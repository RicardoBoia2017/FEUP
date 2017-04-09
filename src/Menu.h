/*
 * Menu.h
 *
 *  Created on: Mar 29, 2017
 *      Author: root
 */

#ifndef MENU_CPP_
#define MENU_CPP_

#include <iostream>

using namespace std;

void ClearScreen() {

#ifdef __unix__                    /* __unix__ is usually defined by compilers targeting Unix systems */

	system("clear");

#elif defined(_WIN32) || defined(WIN32)     /* _Win32 is usually defined by compilers targeting 32 or   64 bit Windows systems */

	system("cls");

#endif

}

bool mainOptions() {
	int option;

	while(1){
		ClearScreen();
		cout << "Menu: " << endl;
		cout << endl;
		cout << "   " << "1 - See the Graph" << endl;
		cout << "   " << "2 - Generate Shopping" << endl;
		cout << "   " << "3 - Show Route" << endl;
		cout << "   " << "4 - Show Route" << endl;
		cout << "   " << "5 - See Clients" << endl;
		cout << "   " << "6 - See Supermarkets" << endl;
		cout << "   " << "7 - Leave" << endl << endl;
		cout << "   " << "Choose your option: ";
		string str_option;
		getline(cin, str_option);
		option = stoi(str_option);

		switch (option){
		case 1:
		{
			SuperMarketChain S = SuperMarketChain();
			S.displayGraph();
			break;
		}
		case 2:
		{
			SuperMarketChain S = SuperMarketChain();
			S.generateShopping();
			S.displayGraph();
			break;
		}
		case 3:{

			break;}
		case 4:{
			break;}
		case 5:{
			break;}
		case 6:{
			break;}
		case 7:{
			return true;}
		default:{
			continue;}
		}
	}
}

#endif /* MENU_CPP_ */
