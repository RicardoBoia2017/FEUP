#include "utils.h"
#include <sstream>
#include <iostream>
#include <fstream>
#include <istream>
#include <math.h>
#include <limits>
#include "excecao.h"
#include "reserva.h"
#include "quicksort.h"

#ifdef __unix__                    /* __unix__ is usually defined by compilers targeting Unix systems */

    #include "linux.h"

#elif defined(_WIN32) || defined(WIN32)     /* _Win32 is usually defined by compilers targeting 32 or   64 bit Windows systems */

    #include <windows.h>
    #include <conio.h>

#endif

using namespace std;


/////////////////////////// LEITURAS ///////////////////////

string lePassword(){

	string password;
	string password_repeated;

	password = getpass("Password: ", true);
	password_repeated = getpass("Confirme Password: ", true);
	try{
		if(password != password_repeated){
			throw PasswordNaoCoincide();
		}
	}
	catch (PasswordNaoCoincide &e) {
		cout << "Apanhou excecao. Passwords Não Coincidem. \n";
		return "";
	}

    return password;
}

string leString(string msg){
	string nome;

	cout << msg;
	getline(cin, nome);

	try{
		if(is_number(nome))
			throw NomeIncorreto(nome);
	}
	catch (NomeIncorreto &e) {
		cout << "Apanhou excecao. Nome Invalido: " << e.getNome() << endl;
		return "";
	}

	return nome;
}


unsigned int leNif() {

	string nif_str;
	unsigned int nif;

	cout << "Nif: ";
	getline(cin, nif_str);

	try{
		if(!is_number(nif_str))
			throw NifInvalido();
	}
	catch (NifInvalido &e) {
		cout << "Apanhou excecao. Nif Invalido" << endl;
		return 0;
	}

	nif = stoi(nif_str);
	double num_digitos = floor(log10(nif)) + 1;

	try{
		if(num_digitos != 9)
			throw NifInvalido(nif);
	}
	catch (NifInvalido &e) {
		cout << "Apanhou excecao. "<< e.getNif() << " não tem 9 digitos." << endl;
		return 0;
	}

	return nif;
}

unsigned short int leUnsignedShortInt(unsigned short int min, unsigned short int  max){
	string opcao_str;
	unsigned int opcao;

	getline(cin, opcao_str);

	try{
		if(!is_number(opcao_str))
			throw OpcaoInvalida();
	}
	catch (OpcaoInvalida &e) {
		cout << "Apanhou excecao. Introduziu um valor invalido" << endl;
		return 0;
	}

	opcao = stoi(opcao_str);

	try{
		if(opcao < min || opcao > max)
			throw OpcaoInvalida(opcao);
	}
	catch (OpcaoInvalida &e) {
		cout << "Apanhou excecao. "<< e.getOpcao() << " não se encontra entre as opções." << endl;
		return 0;
	}

	return opcao;

}

std::string leTipo() {
	int opcao;

	cout << "Tipo de alojamento: " << endl;
	cout << endl;
	cout << "   " << "1 - Apartamento" << endl;
	cout << "   " << "2 - Quarto Hotel" << endl;
	cout << "   " << "3 - Flat" << endl;
	cout << "   " << "4 - Bed­-n-­Breakfast;" << endl;
	cout << "   " << "5 - Shared­House" << endl;
	cout << "   " << "6 - Voltar" << endl << endl;
	cout << "   " << "Qual a sua opcao: ";
	opcao = leUnsignedShortInt(1, 6);

	switch (opcao){
	case 1:
		return "Apartamento";
		break;
	case 2:
		return "Hotel";
		break;
	case 3:
		return "Flat";
		break;
	case 4:
		return "BB";
		break;
	case 5:
		return "Shared";
		break;
	default:
		return "Voltar";
		break;
	}
}

float lePreco() {
	string preco_str;
	float preco;

	cout << "Preco: ";
	getline(cin, preco_str);

	try{
		if(!is_number(preco_str))
			throw PrecoInvalido();
	}
	catch (PrecoInvalido &e) {
		cout << "Apanhou excecao. Preço invalido" << endl;
		return -1;
	}

	preco = stof(preco_str);

	try{
		if(preco < 0)
			throw PrecoInvalido(preco);
	}
	catch (PrecoInvalido &e) {
		cout << "Apanhou excecao. "<< e.getPreco() << " não é um preço válido." << endl;
		return 0;
	}

	return preco;

}

std::vector<Reserva> leReservas(float preco) {
	vector <Reserva> reservas;
	int opcao;
	string data;
	Data D1;
	Data D2;
	unsigned int counter = 0;

	while(1){

		unsigned int diff;

		cout << "Adicionar Datas Ocupadas? " << endl;
		cout << endl;
		cout << "   " << "1 - Sim" << endl;
		cout << "   " << "2 - Não" << endl;

		opcao = leUnsignedShortInt(1, 2);

		if(opcao == 2)
			break;
		else if(opcao == 0){
			cout << "Leitura Interrompida" << endl;
			break;
		}

		else{
			cout << "Data Inicial (dd/mm/aaaa): ";

			getline(cin, data, ' ');

			D1 = string2data(data);

			if(D1.getDia()==0){
				cout << "Leitura Interrompida" << endl;
				break;
			}

			cout << endl;

			cout << "Data Final (dd/mm/aaaa): ";
			getline(cin, data, ' ');

			D2 = string2data(data);

			if(D2.getDia()==0){
				cout << "Leitura Interrompida" << endl;
				break;
			}

			cout << endl;
			if(swapDatas(&D1,&D2))
				cout << "Detetamos que colocou uma Data Inicial mais recente que a Data Final." << endl <<
				"Não se preocupe. Já corrigimos o erro por si." <<  endl;

			Reserva R(D1, D2, preco);
			reservas.push_back(R);

			diff = D1 - D2;
			if(diff == 0)
				counter +=1;
			else
				counter += diff;
		}
	}
	cout << counter << " datas adicionadas. "<< endl;
	return reservas;
}

bool leExtrasApartamento(bool* suite, bool* cozinha, bool* sala_de_estar,
		int* quartos) {

	int opcao;

	cout << "O Apartamento Tem Suite? " << endl;
	cout << endl;
	cout << "   " << "1 - Sim" << endl;
	cout << "   " << "2 - Não" << endl;

	opcao = leUnsignedShortInt(1, 2);

	switch(opcao){
	case 0:
		return false;
	case 1:
		*suite = true;
		break;
	default:
		*suite = false;
	}

	cout << "O Apartamento Tem Cozinha? " << endl;
	cout << endl;
	cout << "   " << "1 - Sim" << endl;
	cout << "   " << "2 - Não" << endl;

	opcao = leUnsignedShortInt(1, 2);

	switch(opcao){
	case 0:
		return false;
	case 1:
		*cozinha = true;
		break;
	default:
		*cozinha = false;
	}

	cout << "O Apartamento Tem Sala de Estar? " << endl;
	cout << endl;
	cout << "   " << "1 - Sim" << endl;
	cout << "   " << "2 - Não" << endl;

	opcao = leUnsignedShortInt(1, 2);

	switch(opcao){
	case 0:
		return false;
	case 1:
		*sala_de_estar = true;
		break;
	default:
		*sala_de_estar = false;
	}

	cout << "Quantos quartos tem o apartamento?" << endl;
	cout << endl;


	opcao = leUnsignedShortInt(1, numeric_limits<unsigned int>::max());

	switch(opcao){
	case 0:
		return false;
	default:
		*quartos = opcao;
	}

	return true;
}

bool leExtrasHotel(int* cama, bool* cama_extra) {

	int opcao;

	cout << "Tipo de Quarto de Hotel? " << endl;
	cout << endl;
	cout << "   " << "1 - Quarto Simples" << endl;
	cout << "   " << "2 - Quarto Duplo" << endl;
	cout << "   " << "3 - Quarto Duplo com Cama Adicional" << endl;
	cout << "   " << "4 - Quarto Triplo" << endl;

	opcao = leUnsignedShortInt(1, 4);

	switch(opcao){
	case 0:
		return false;
	case 1:
		*cama = 1;
		*cama_extra = false;
		break;
	case 2:
		*cama = 2;
		*cama_extra = false;
		break;
	case 3:
		*cama = 2;
		*cama_extra = true;
		break;
	case 4:
		*cama = 3;
		*cama_extra = false;
		break;
	default:
		*cama = 1;
		*cama_extra = false;
		break;
	}
	return true;
}


/////////////////////////// LEITURAS AUXILIARES ///////////////////////

string getpass(const char *prompt, bool show_asterisk)
{
  const char BACKSPACE=127;
  const char RETURN=10;

  string password;
  unsigned char ch=0;

  cout <<prompt;

  while((ch=getch())!=RETURN)
    {
       if(ch==BACKSPACE)
         {
            if(password.length()!=0)
              {
                 if(show_asterisk)
                 cout <<"\b \b";
                 password.resize(password.length()-1);
              }
         }
       else
         {
             password+=ch;
             if(show_asterisk)
                 cout <<'*';
         }
    }
  cout <<endl;
  return password;
}


bool is_number(const string& s)
{
	long double num;
	istringstream iss(s);
	return !(iss >> num).fail();
}

/////////////////////////// DATA AUXILIARES ///////////////////////

bool ano_bissexto(unsigned int ano)
{
	if (((ano % 4 == 0) && (ano % 100 != 0)) || (ano % 400 == 0))
		return true;

	return false;
}

bool data_valida(unsigned int dia, unsigned int mes, unsigned int ano){
	if(ano > 1990){
		if(mes>0 && mes<13){
			if(dia_valido(dia,mes,ano))
				return true;
		}
	}
	return false;
}

bool dia_valido(unsigned int dia, unsigned int mes, unsigned int ano)
{
	if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12)
		if (dia > 0 && dia <= 31)
			return true;
		else
			return false;
	else if (mes != 2)
		if (dia > 0 && dia <= 30)
			return true;
		else
			return false;
	else // mes == 2
		if (ano_bissexto(ano))
			if (dia > 0 && dia <= 29)
				return true;
			else
				return false;
		else
			if (dia > 0 && dia <= 28)
				return true;
			else
				return false;
}

bool dias_sobrepostos(Data d1, Data d2, Data d3, Data d4) {
	if(((d3 < d1) && (d4 < d1)) || ((d2 < d3) && (d2 < d4)))
		return true;
	else
		return false;
}

Data string2data(string data){
	unsigned int dia;
	unsigned int mes;
	unsigned int ano;

	try{
		dia = stoi(data.substr(0,2));
	}
	catch (...){
		cout << "Apanhou excecao. Data não convertivel." << endl;
		return Data(0,0,0);
	}
	try{
		mes = stoi(data.substr(3,2));
	}
	catch (...){
		cout << "Apanhou excecao. Data não convertivel." << endl;
		return Data(0,0,0);
	}

	try{
		ano = stoi(data.substr(6,4));
	}
	catch (...){
		cout << "Apanhou excecao. Data não convertivel." << endl;
		return Data(0,0,0);
	}

	try{
		if(!data_valida(dia,mes,ano) )
			throw dataInvalida(data);
	}
	catch (dataInvalida &e) {
		cout << "Apanhou excecao. "<< e.getData() << " não é uma data válida." << endl;
		return Data(0,0,0);
	}

	//cout << dia << "/" << mes << "/" << ano << endl;

	Data D(dia, mes, ano);

	return D;
}

string data2string(Data data){
	string data_str;

	data_str = to_string_special(data.getDia()) + '/' +
			to_string_special(data.getMes()) + '/' +
			to_string(data.getAno());

	return data_str;
}

string to_string_special(unsigned int num)
{
	double num_digitos = floor(log10(num)) + 1;
	string num_str = to_string(num);

	if (num_digitos == 1.0)
	{
		num_str = "0" + num_str;
	}

	return num_str;
}

bool swapDatas(Data* dataInicio, Data* dataFim) {
	if (*dataFim < *dataInicio){
		swap(*dataInicio, *dataFim);
		return true;
	}
		return false;
}

int countLeapYears(Data d)
{
    int years = d.getAno();

    if (d.getMes() <= 2)
        years--;

    return years / 4 - years / 100 + years / 400;
}


vector<Imovel*> ordenaMontra(vector<Imovel*> &montra, bool LowestFirst){
	QuickSort <Imovel*> QS(&montra, 0, montra.size() - 1, LowestFirst);
	return montra;

}

void ClearScreen() {

#ifdef __unix__                    /* __unix__ is usually defined by compilers targeting Unix systems */

	system("clear");

#elif defined(_WIN32) || defined(WIN32)     /* _Win32 is usually defined by compilers targeting 32 or   64 bit Windows systems */

	system("cls");

#endif

}
