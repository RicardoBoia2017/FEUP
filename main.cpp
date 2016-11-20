#include <iostream>
#include <fstream>
#include <string>

#include "broker.h"
#include "data.h"
#include "interacao.h"
#include "utils.h"
#include "imovel.h"


using namespace std;


int main() {


	string nome;
	string filename;
	fstream f;

	cout << "Bem-Vindo" << endl << endl;

	cout << "Broker: ";
	getline(cin, nome);

	filename = nome + ".txt";

	f.open(filename);

	if(f.is_open()){
		cout << endl << "Carregando base de dados..." << endl << endl;

		string ficheiroClientes;
		string ficheiroFornecedores;
		string receita_str;
		float receita;

		getline(f, nome);
		getline(f, ficheiroClientes);
		getline(f, ficheiroFornecedores);
		getline(f, receita_str);

		cout << nome << endl;
		cout << ficheiroClientes << endl;
		cout << ficheiroFornecedores << endl;
		cout << stof(receita_str) << endl;

		cout << endl << endl;

		f.close();

//    	Broker Existente(nome, ficheiroClientes, ficheiroFornecedores, stof(receita_str));

//		Existente.adicionaFornecedor();

//		cout << endl << endl;

//		Existente.mostraMontra();


	}
	else{
		cout << endl << "Gerando base de dados..." << endl << endl;
		Broker Novo(nome);

	}

	return 0;

}
