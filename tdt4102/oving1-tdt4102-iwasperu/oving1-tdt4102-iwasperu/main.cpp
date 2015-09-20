//
//  main.cpp
//  oving1-tdt4102-iwasperu
//
//  Created by Iver Egge on 1/21/13.
//  Copyright (c) 2013 Iver Egge. All rights reserved.
//

#include <iostream>
#include <cmath>
using namespace std;
int part1();
int part2();
int part3();
int part4();

int main(int argc, const char * argv[])
{
    //part1();
    //part2();
    //part3();
    part4();
    return 0;
}

int part1() {
    // task a)
    int j;
    cout << "Write an integer number: ";
    cin >> j;
    cout << "You wrote: " << j;
    cout << "\n\n";

    // task b)
    int a, b;
    cout << "Write an integer number: ";
    cin >> a;
    cout << "Write another integer number: ";
    cin >> b;
    cout << "The sum of the two integers is: " << a + b;
    cout << "\n\n";

    return 0;
}

int part2() {
    int h, m ,s;
    cout << "Write a number of seconds: ";
    cin >> s;
    h = s / 3600;
    s = s - (h * 3600);
    m = s / 60;
    s = s - (m * 60);
    cout << h << " hours, " << m << " minutes and " << s << " seconds";
    cout << "\n\n";
    
    double cost, tax, tip;
    cout << "Cost of meal without tax and tip: ";
    cin >> cost;
    tax = cost * 0.0675;
    tip = (cost + tax) * 0.15;
    cout << "Cost of meal: " << cost << "\n";
    cout << "Tax: " << tax << "\n";
    cout << "Tip: " << tip << "\n";
    cout << "Total: " << cost + tax + tip << "\n";
    cout << "\n\n";
    
    double x, y;
    cout << "Provide x: ";
    cin >> x;
    y = pow(x,2) + ((6* x - 2)/3);
    cout << "y: " << y << "\n";
    cout << "\n\n";
    
    return 0;
}

int part3() {
    int n;
    cout << "Write a number: ";
    cin >> n;
    if (n % 2 == 0) {
        cout << "The number is even";
    } else {
        cout << "The number is odd";
    }
    cout << "\n\n";
    
    double n1, n2, largest;
    cout << "Write a number: ";
    cin >> n1;
    cout << "Write another number: ";
    cin >> n2;
    if (n1 > n2) {
        largest = n1;
    } else {
        largest = n2;
    }
    cout << "The largest number is " << largest << "\n";
    cout << "\n\n";

    return 0;
}

int part4() {
    // task a)
    double rate = 9.22;
    double value = -99;
    while (value != 0) {
        cout << "Input value in NOK (0 to continue): ";
        cin >> value;
        if (value > 0) {
            cout << value << " NOK is: " << value / rate << " Euro" << endl;
        }
    }
    cout << "\n\n";

    // task b)
    cout << "N\t" << "10*N\t" << "100*N\t" << "1000*N\n";
    int N = 1;
    while (N <= 5) {
        cout << N << "\t" << N*10 << "\t\t" << N*100 << "\t\t" << N*1000 << endl;
        N++;
    }
    cout << "\n\n";
    
    // task c)
    double amount, interest, payment, remaining;
    int number_of_payments = 10;
    cout << "Input amount: ";
    cin >> amount;
    cout << "Input interest (%): ";
    cin >> interest;
    cout << "Year\t" << "Payment\t" << "Remaining\n";
    for (int i = 0; i < number_of_payments; i++) {
    payment = (amount/number_of_payments) + (interest/100.0)*(amount)*((10.0-i)/10.0);
        //amount = amount - payment;
        remaining = amount * (9-i)/10;
        cout << i+1 << "\t\t"<< payment << "\t\t" << remaining << endl;
    }
    cout << "\n\n";

    // task d)
    int height, width;
    cout << "Enter height: ";
    cin >> height;
    cout << "Enter width: ";
    cin >> width;
    
    for (int i = 1; i <= height; i++) {
        for (int j = 1; j <= width; j++) {
            cout << i*j << "\t";
        }
        cout << endl;
    }
    cout << "\n\n";

    return 0;
}
