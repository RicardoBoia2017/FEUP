#ifndef CLIENTE_H_
#define CLIENTE_H_

#include <string>

class Cliente{
	std::string nome;
	int pontos;
	float valor;
	static unsigned int counter;
public:
	Cliente(std::string nome);
	Cliente(std::string nome, int pontos, float valor);
	std::string getNome() const;
	int getPontos() const;
	float getValor() const;
	static unsigned int getTotalClientes();
	void setPontos(int pontos);

};

class Registado: public Cliente{
	std::string password;
public:
	Registado(std::string nome, std::string password);
	Registado(std::string nome, int pontos, float valor, std::string password);
	std::string getPassword() const;
};



#endif /* CLIENTE_H_ */
