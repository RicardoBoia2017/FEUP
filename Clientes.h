#ifndef CLIENTES_H_
#define CLIENTES_H_

#include <string>

class Cliente{
	std::string nome;
public:
	Cliente(std::string nome);
	std::string getNome() const;
};


#endif /* CLIENTES_H_ */
