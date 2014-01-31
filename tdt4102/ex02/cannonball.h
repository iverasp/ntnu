#ifndef CANNONBALL_H_INCLUDED
#define CANNONBALL_H_INCLUDED

double acclY();
double acclX();
double velY(double v0, double t);
double velX(double v0, double t);
double velIntY(double v0, double t);

double posX(double v0, double t);
double posIntX(double v0, double t);
double posY(double v0, double t);
double posIntY(double v0, double t);

void printTime(double t);
double flightTime(double v0y);

void getUserInput(double *theta, double *absVelocity);

double getVelocityX(double theta, double absVelocity);
double getVelocityY(double theta, double absVelocity);

void getVelocityVector(double theta, double absVelocity, double *velocityX, double *velocityY);

double getDistanceTraveled(double velocityX, double velocityY);
double optimalAngleForMaxDistance(double absVelocity);
double targetPractice(double distanceToTarget, double velocityX, double velocityY);

void playTargetPractice();

#endif
