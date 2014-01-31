#include <iostream>
#include <math.h>
#include "cannonball.h"

using namespace std;

double dx = 0.01;

double acclY() {
    return -9.81;
}

double acclX() {
    return 0.;
}

double velY(double v0, double t) {
    return v0 + acclY() * t;
}

double velX(double v0, double t) {
    return v0 + acclX() * t;
}

double velIntY(double v0, double t) {
    double vy = v0;
    int steps = t / dx - 1;
    
    for (int i = 0; i < steps; i++) {
        vy += acclY() * dx;
    }
    
    return vy;
}

double posX(double v0, double t) {
    return v0 * t + ((acclX() * pow(t, 2)) / 2.);
}

double posIntX(double v0, double t) {
    double px = 0.;
    int steps = t / dx - 1;
    double tt = 0.;
    
    for (int i = 0; i < steps; i++) {
        tt += dx;
        px += velX(v0, tt) * dx;
    }
    
    return px;
}

double posY(double v0, double t) {
    return v0 * t + ((acclY() * pow(t, 2)) / 2.);
}

double posIntY(double v0, double t) {
    double py = 0.;
    int steps = t / dx - 1;
    double tt = 0.;

    for (int i = 0; i < steps ; i++) {
        tt += dx;
        py += velY(v0, tt) * dx;
    }
    
    return py;
}

void printTime(double t) {
    int h, m;
    h = t / 3600;
    t = t - (h * 3600);
    m = t / 60;
    t = t - (m * 60);
    cout << h << " hours, " << m << " minutes and " << t << " seconds";
    
}

double flightTime(double v0y) {
    return 2 * ( (v0y / ((-1) * acclY()) ) );
}

void getUserInput(double *theta, double *absVelocity) {
    cout << "enter angle: ";
    cin >> *theta;
    cout << "enter velocity: ";
    cin >> *absVelocity;
}

double getVelocityX(double theta, double absVelocity) {
    return absVelocity * cos(theta);
}

double getVelocityY(double theta, double absVelocity) {
    return absVelocity * sin(theta);
}

void getVelocityVector(double theta, double absVelocity, double *velocityX, double *velocityY) {
    *velocityX = getVelocityX(theta, absVelocity);
    *velocityY = getVelocityY(theta, absVelocity);
}

double getDistanceTraveled(double velocityX, double velocityY) {
    double t = flightTime(velocityY);
    return posX(velocityX, t);
}

double optimalAngleForMaxDistance(double absVelocity) {
    double vy, vx, tmp;
    double d = 0.;
    double mt = 0.;

    for (double t = 0; t <= 90; t++) {
        vx = getVelocityX(mt, absVelocity);
        vy = getVelocityY(mt, absVelocity);
        tmp = getDistanceTraveled(vx, vy);
        if (tmp > d) {
            mt = t;
            d = tmp;
        }
    }

    return mt;
}

double targetPractice(double distanceToTarget, double velocityX, double velocityY) {
    return distanceToTarget - getDistanceTraveled(velocityX, velocityY);
}

void playTargetPractice();

