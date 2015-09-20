import std.stdio;
import std.conv;
import std.string;

int mst(int[][] adj_matrix) {

    return 0;
}

int main() {

    const int INF = int.max;

    string buf;

    string[] input;

    while(stdin.readln(buf)) {
        input ~= buf;
    }

    int input_length = to!int(input.length);

    int[][] adj_matrix = new int[][](input_length, input_length);

    for (int i = 0; i < input_length; i++) {
        for (int j = 0; j < input_length; j++) {
            adj_matrix[i][j] = INF;
        }
    }

    string[] temp;

    for (int i = 0; i < input_length; i++) {
        temp = split(input[i]);
        for (int j = 0; j < temp.length; j++) {
            string[] one_edge = split(temp[j], ":");
            adj_matrix[i][to!int(one_edge[0])] = to!int(one_edge[1]);
        }
    }

    //write("");
    writeln(mst(adj_matrix));

    return 0;

}
