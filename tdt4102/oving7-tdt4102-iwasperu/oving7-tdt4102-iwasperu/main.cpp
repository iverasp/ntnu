#include <iostream>
#include <fstream>
#include <string>

int write_strings_to_file();
int read_strings_from_file();
int character_statistics();

using namespace std;

string filepath = "oving7.txt";

int main() {
    
    write_strings_to_file();
    read_strings_from_file();
    character_statistics();
    
    return 0;
}

int write_strings_to_file() {
    
    string line;
    ofstream file;
    file.open("oving7.txt");
    
    cout << "Input text, line by line" << endl;
    cout << "It will be appended to the file " << filepath << endl;
    
    while(line.compare("quit") != 0) {
        cin >> line;
        if (line.compare("quit") != 0) file << line << "\n";
    }
    
    file.close();
    
    return 0;
}

int read_strings_from_file() {
    
    string line;
    ifstream ifile;
    ifile.open("oving7.txt");
    
    int line_number = 1;
    ofstream ofile;
    ofile.open("oving7_line_numbers.txt");
    
    if (ifile.is_open()) {
        while(ifile.good()) {
            getline(ifile,line);
            cout << line << endl;
            ofile << line_number << " " << line << "\n";
            line_number++;
        }
        ifile.close();
        ofile.close();
    } else {
        cout << "File not found" << endl;
    }
    
    return 0;
}

int character_statistics() {
    string line;
    ifstream file ("oving7.txt");
    static const int span = ('z' - 'a' + 1);
    int characters[span] = {0};
    int number_of_characters = 0;
    
    if (file.is_open()) {
        while (file.good()) {
            getline (file, line);
            for (int i = 0; i < line.length(); i++) {
                characters[line[i] - 'a'] += 1;
                number_of_characters++;
            }
        }
    } else {
        cout << "File not found" << endl;
    }
    
    cout << "\nCharacter statistics:" << endl;
    cout << "Total number of characters: " << number_of_characters << endl;
    for (int i = 0; i < span; i++) {
        cout << (char)(i + 'a') << ": " << characters[i] << endl;
    }
    
    return 0;
}
