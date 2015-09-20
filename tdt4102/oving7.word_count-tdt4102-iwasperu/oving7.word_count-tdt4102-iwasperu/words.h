#include <iostream>
#include <string>

using namespace std;

class Words {
public:
    Words(string word);
    int getCount();
    string getWord();
    int addCount();
private:
    int count;
    string word;
};

Words::Words(string word) {
    this->word = word;
    count = 1;
}

int Words::getCount() {
    return count;
}

int Words::addCount() {
    count++;
    return 0;
}

string Words::getWord() {
    return word;
}