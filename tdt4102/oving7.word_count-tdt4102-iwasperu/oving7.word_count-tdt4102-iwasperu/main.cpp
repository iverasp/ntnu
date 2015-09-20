#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include "words.h"

int add_word(vector<Words> &words, string word);
string clear_word(string word);
int print_statistics(vector<Words> &words);

using namespace std;

int line_numbers = 0;
long longest_word = 0;

int main(int argc, const char * argv[])
{
    vector<Words> words;
    string word;
    
    string line;
    ifstream ifile;
    ifile.open("1ch.av");
    
    if (ifile.is_open()) {
        while(ifile.good()) {
            getline(ifile,line);
            int pos = 0;
            for (int i = 0; i < line.size(); i++) {
                if (isspace(line[i])) {
                    add_word(words, clear_word(line.substr(pos, i - pos)));
                    pos = i + 1;
                }
            }
            
            line_numbers++;
        }
        ifile.close();
    } else {
        cout << "File not found" << endl;
    }
    
    print_statistics(words);
    
    return 0;
}

string clear_word(string word) {
    
    for (int i = 0; i < word.size(); i++) {
        //if (ispunct(word[i]) || isnumber(word[i])) {
        if (!isalpha(word[i])) {
            word.erase(i, 1);
        }
        word[i] = tolower(word[i]);
    }
    return word;
}

int add_word(vector<Words> &words, string word) {
    bool has_word = false;
    
    if (word.size() == 0) return 0;
    
    for (int i = 0; i < words.size(); i++) {
        if (words[i].getWord().compare(word) == 0) {
            has_word = true;
            words[i].addCount();
        }
    }
    
    if (!has_word) words.push_back(word);
    
    return 0;
}

int print_statistics(vector<Words> &words) {
    long longest_word_length = 0;
    string longest_word;
    float total_words_length = 0;
    
    for (int i = 0; i < words.size(); i++) {
        total_words_length += words[i].getWord().size();
        if (words[i].getWord().size() > longest_word_length) {
            longest_word = words[i].getWord();
            longest_word_length = words[i].getWord().size();
        }
    }
    
    cout << "Text statistics:" << endl;
    cout << "Longest word: " << longest_word << endl;
    cout << "Number of words: " << words.size() << endl;
    cout << "Number of lines: " << line_numbers << endl;
    cout << "Average word length: " << total_words_length / words.size() << endl;
    
    for (int i = 0; i < words.size(); i++) {
        cout << words[i].getWord() << ": " << words[i].getCount() << endl;
    }
    
    return 0;
}



