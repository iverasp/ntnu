// iver_

import std.stdio;
import std.conv;
import std.string;

real subgraph_density(bool[][] adj_matrix, int start_node, int n) {

    int i;
    int j;
    int nodes_size_w = n - 1;
    bool[] w = new bool[n];
    
    int[] queue = new int[70];
    int queue_length = 0;

    w[start_node] = true;

    queue_length++;
    queue[queue_length - 1] = start_node;

    int current_index = 0;
    int current_node;

    while (current_index != queue_length) {
        current_node = queue[current_index++];

        for (j = 0; j < n; j++) {
            if (adj_matrix[current_node][j] && !w[j]) {
                w[j] = true,
                nodes_size_w--;
                queue_length++;
                queue[queue_length - 1] = j;
            }
        }
    }

    if (nodes_size_w == 0) return 0.000;

    int edge_amount = 0;

    for (i = 0; i < n; i++) {
        if (!w[i]) {
            for (j = 0; j < n; j++) {
                if (adj_matrix[i][j] && !w[j]) {
                    edge_amount++;
                }
            }
        }
    }

    return to!real(edge_amount) / (to!real(nodes_size_w * nodes_size_w));
}


int main() {

    char[] buf;

    int n = to!int(strip(stdin.readln()));
    bool[][] adj_matrix = new bool[][](n, n);
    string adj_row;

    for (int i = 0; i < n; i++) {
        adj_row = strip(stdin.readln());
        for (int j = 0; j < n; j++) {
            adj_matrix[i][j] = adj_row[j] == '1';
            //if (adj_row[j] == '1') adj_matrix[i][j] = true;
        }
    }
    
    while(stdin.readln(buf)) {
        int start_node = to!int(strip(buf));
        writefln("%.3f", subgraph_density(adj_matrix, start_node, n) + cast(real)1E-12);
    }

    return 0;
}
