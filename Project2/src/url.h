#ifndef URL_H
#define URL_H

#define FTP_PORT 21
#define MAX_SIZE 256

typedef struct {
	char* user; 
	char* password;
	char* host; 
	char* ip; 
	char* path;
	int port;
} URL;

void initURL(URL* url);
int parseURL(const char* urlStr, URL* url);
int processURL(const char* urlStr, URL* url);

void showURL(URL* url);


#endif
