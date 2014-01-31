//
//  main.cpp
//  tdt4102-exercise02
//
//  Created by Iver Egge on 1/22/14.
//  Copyright (c) 2014 Iver Egge. All rights reserved.
//

#include <iostream>
#include "cannonball.h"
#include <stdlib.h>
#include <math.h>

using namespace std;

double v0y = 25.;
double v0x = 50.;

int main(int argc, const char * argv[])
{

    //cout << posIntY(v0y, 5.) << endl; // f
    //cout << posIntX(v0x, 5.) << endl; // r
    //cout << velIntY(v0y, 5.) << endl; // r
    //cout << velX(v0x, 5.) << endl; // r
    //cout << posX(v0x, 5.) << endl; // r
    //cout << posY(v0y, 5.) << endl; // r
    //cout << flightTime(860) << endl;

    double target = rand() % 10 + 60;
    int tries = 10;
    bool hit = false;
    double v0, theta, vx, vy, d;

    for (int i = 0; i < 10; i++) {

        getUserInput(&theta, &v0);
        vy = getVelocityY(theta, v0);
        vx = getVelocityX(theta, v0);
        d = targetPractice(target, vx, vy);

        if (floor(d) == 0) {
            cout << "Hit!" << endl;
            return 0;
        }
        cout << "Missed by " << d << endl;

    }

    cout << "You lost" << endl;


    return 0;
}

