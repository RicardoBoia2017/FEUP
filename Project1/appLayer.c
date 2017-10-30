
#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <termios.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>
#include "dataLinkLayer.h"
#include "appLayer.h"


int appLayer(ApplicationLayer* applicationLayer, LinkLayer* linkLayer, FileData* file){

  if (openSerialPort(applicationLayer, linkLayer) == -1) {
    printf("appLayer: openSerialPort \n");
    return -1;
  }

  if (setNewTermiosStructure(applicationLayer, linkLayer) == -1) {
    printf("appLayer: setNewTermiosStructure \n");
    return -1;
  }

  if(llopen(applicationLayer, linkLayer)<0)
    return -1;

  printf("Connection Established!\n");
    if(applicationLayer->status == TRANSMITTER){
      if(sendData(applicationLayer, linkLayer, file)<0)
        return -1;
    }
    else if(applicationLayer->status == RECEIVER){
      if(receiveData(applicationLayer, linkLayer, file)<0)
        return -1;
    }


  return llclose(applicationLayer, linkLayer);
}



int sendData(ApplicationLayer* applicationLayer, LinkLayer* linkLayer, FileData* file){


  FILE* fp;

  fp = fopen(file->name, "rb");
  if (!fp) {printf("sendData: open error \n"); return -1; }

  if (DEBUG_MODE)
  printf("sendData: opened file %s\n", file->name);

  file->size = getFileSize(fp);
  if(file->size < 0) {printf("sendData: getFileSize error \n"); return -1; }


  if((sendControlPackage(CTRL_PACKET_START, file, applicationLayer, linkLayer)) < 0)
    {printf("sendData: sendControlPackage error \n"); return -1; }


  char* dataBuffer = malloc(MAX_SIZE);
  int readBytes = 0;
  int i = 0;

  while ((readBytes = fread(dataBuffer, sizeof(char), MAX_SIZE, fp)) > 0) {
    if ((sendDataPackage(dataBuffer, (i++) % 255, readBytes, applicationLayer, linkLayer)) < 0) {
      printf("sendData: sendDataPackage error \n");
      free(dataBuffer);
      return -1;
    }

    dataBuffer = memset(dataBuffer, 0, MAX_SIZE);
    linkLayer->sequenceNumber = !linkLayer->sequenceNumber;

  }

  free(dataBuffer);
  if (fclose(fp) != 0)
    {printf("sendData: fclose error \n"); return -1; }

  if(sendControlPackage(CTRL_PACKET_END, file, applicationLayer, linkLayer) < 0)
    {printf("sendData: sendControlPackage error \n"); return -1; }


  printf("File successfully transferred!\n");
  printf("File Name: %s\n", file->name);
  printf("File Size: %d\n", file->size);
  return 0;
}


int sendControlPackage(int controlField, FileData* file, ApplicationLayer* applicationLayer, LinkLayer* linkLayer){

  char* fileSize = malloc(MAX_SIZE);
  memcpy(fileSize, &(file->size), sizeof(file->size));

  int packageSize = 5 + strlen(fileSize) + strlen(file->name); // 5 <-> C, T1, L1, T2, L2
  int pos = 0;

  unsigned char controlPackage[packageSize];
  controlPackage[pos++] = controlField;
	controlPackage[pos++] = T_FILE_SIZE;
  controlPackage[pos++] = strlen(fileSize);

  for(int i = 0; i < strlen(fileSize); i++){
      controlPackage[pos++] = fileSize[i];
  }

  controlPackage[pos++] = T_FILE_NAME;
  controlPackage[pos++] = strlen(file->name);

  int i;
  for (i = 0; i < strlen(file->name); i++)
    controlPackage[pos++] = file->name[i];

  if (DEBUG_MODE)
  printf("sendControlPackage: Start sending Control Package: \n");

  if(llwrite(applicationLayer, linkLayer, controlPackage, packageSize) < 0)
    {printf("sendControlPackage: llwrite \n"); return -1; }

  return 0;

}


int sendDataPackage(char* buffer, int N, int length, ApplicationLayer* applicationLayer, LinkLayer* linkLayer){

  unsigned char C = CTRL_PACKET_DATA;
  unsigned char L2 = length / 256;  //K = 256 * L2+ L1
  unsigned char L1 = length % 256;

  int packageSize = 4 + length; // 4 <-> C, N, L2, L1

  unsigned char* package = (unsigned char*) malloc(packageSize);

  package[0] = C;
  package[1] = N;
  package[2] = L2;
  package[3] = L1;

  memcpy(&package[4], buffer, length);

  if (DEBUG_MODE)
  printf("sendDataPackage: Start sending Data Packet: \n");

  if(llwrite(applicationLayer, linkLayer, package, packageSize) < 0)
    {printf("sendDataPackage: llwrite error \n"); free(package); return -1; }

  free(package);

  return 0;

}

int receiveData(ApplicationLayer* applicationLayer, LinkLayer* linkLayer, FileData* file ){

  FILE* outFile;
  int controlStart;
  int ret;

  if (DEBUG_MODE)
  printf("receiveControlPackage: Start looking for Control Package \n");

  numTries = 0;
  flagAlarm = 1;

  while(numTries < linkLayer->numTransmissions && flagAlarm){
    ret = receiveControlPackage(&controlStart, file, applicationLayer, linkLayer);

    if(controlStart != CTRL_PACKET_START)
    ret = -1;

    if(ret < 0){
      if (DEBUG_MODE)
        printf("receiveData: CTRL_PACKET_START not received \n");
      write(applicationLayer->fileDescriptor, REJ0, 5);
    }
    else{
      if (DEBUG_MODE){
        printf("receiveData: CTRL_PACKET_START received \n");
        printf("receiveData: Sending confirmation \n");
      }
      write(applicationLayer->fileDescriptor, RR0, 5);
      linkLayer->sequenceNumber = 0; //workaround to not change sequenceNumber after reading CTRL_PACKET_START
      break;
    }

  }

  if(ret<0)
    return -1;


  outFile = fopen(file->name, "wb");

  if(!outFile){
    printf("receiveData: Open Error");
    return -1;
  }

  if (DEBUG_MODE)
  printf("receiveData: file created \n");


  int fileRead = 0;
  int N = -1;
  while(fileRead < file->size){

    int lastN = N;
    char* dataBuffer = NULL;
    int length = 0;

    if (DEBUG_MODE)
    printf("receiveData: Start looking for Data Package: \n");
    ret = receiveDataPackage(&N, &dataBuffer, &length, applicationLayer, linkLayer);

    if (DEBUG_MODE)
    printf("receiveData: Package received \n");

    if (N != 0 && lastN + 1 != N) {
      if (DEBUG_MODE)
        printf("receiveData: Found Duplicated \n");
      ret = -1;
    }

    if(ret < 0){
      (linkLayer->sequenceNumber == 0) ? write(applicationLayer->fileDescriptor, REJ0, 5): write(applicationLayer->fileDescriptor, REJ1, 5);
      if (DEBUG_MODE){
        (linkLayer->sequenceNumber == 0) ? printf("receiveData: Package rejected com REJ0 \n"): printf("receiveData: Package rejected com REJ1 \n");
      }
      numSentREJ++;
      continue;
    }

    fwrite(dataBuffer, sizeof(char), length, outFile);
    free(dataBuffer);

    fileRead += length;

    (linkLayer->sequenceNumber == 0) ? write(applicationLayer->fileDescriptor, RR0, 5): write(applicationLayer->fileDescriptor, RR1, 5);
    if (DEBUG_MODE){
      (linkLayer->sequenceNumber == 0) ? printf("receiveData: Package accepted com RR0 \n"): printf("receiveData: Package accepted com RR1 \n");
    }
    numSentRR++;

    linkLayer->sequenceNumber = !linkLayer->sequenceNumber;
  }

  if(fclose(outFile) != 0){
    printf("receiveData: Closing File Error \n");
    return -1;
  }

  printf("File successfully transferred!\n");
  printf("File Name: %s\n", file->name);
  printf("File Size: %d\n", file->size);
  return 0;
}

int receiveControlPackage(int* controlPackageType, FileData* file, ApplicationLayer* applicationLayer, LinkLayer* linkLayer){

  int totalSize = llread(applicationLayer, linkLayer);

  if(totalSize < 0){
    printf("receiveControlPackage: llread error \n");
    return -1;
  }

  int i;
  int numParams = 2;
  int pos = 4;
  int numberOfBytes;

  *controlPackageType = linkLayer->frame[pos++];

  for(i = 0; i < numParams; i++){
    int paramType = linkLayer->frame[pos++];

    switch(paramType){
      case T_FILE_SIZE:
        numberOfBytes = (int) linkLayer->frame[pos++];
        memcpy(&(file->size), linkLayer->frame + pos, numberOfBytes);
        pos += numberOfBytes;
        break;

      case T_FILE_NAME:
        numberOfBytes = (int) linkLayer->frame[pos++];
        memcpy(&(file->name), linkLayer->frame + pos, numberOfBytes);;
        pos += numberOfBytes;
        break;
    }
  }
  return 0;
}

int receiveDataPackage(int* N, char** buf, int* length, ApplicationLayer* applicationLayer, LinkLayer* linkLayer){

  int size = llread(applicationLayer, linkLayer);

  if(size < 0){
    printf("receiveDataPackage: llread error \n");
    return -1;
  }

  int C = linkLayer->frame[4];
  *N = (int) linkLayer->frame[5];
  int L2 = linkLayer->frame[6];
  int L1 = linkLayer->frame[7];

  if(C != CTRL_PACKET_DATA){
      printf("receiveDataPackage: not a data package \n");
    return -1;
  }

  *length = 256 * L2 + L1;
  *buf = malloc(*length);

  if (linkLayer->frame[8 + *length] != getBCC2(&linkLayer->frame[4], *length + 4)) {
    printf("receiveDataPackage: BCC2 error \n");
    return -1;
}

  memcpy(*buf, &linkLayer->frame[8], *length);

  return 0;
}


void showStats(LinkLayer* linkLayer, FileData* file, double timeElapsed){

	printf("\n");
	printf("----------- STATISTICS -----------\n");
  printf("Filename: %s\n", file->name);
  printf("File Size: %d\n", file->size);
  printf("Time Elapsed: %f\n", timeElapsed);
	printf("Sent RR: %d\n", numSentRR);
	printf("Received RR: %d\n", numReceivedRR * 2); //lol
	printf("Sent REJ: %d\n", numSentREJ);
	printf("Received REJ: %d\n", numReceivedREJ);
	printf("----------------------------------\n");
	printf("\n");


}
