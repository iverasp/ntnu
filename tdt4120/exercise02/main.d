import std.stdio;
import std.string;
import std.conv;

// node class
class Node {
    bool ratatosk;
    Node[] child;
    int child_length;
    int depth;

    this() {
        this.child = new Node[10];
        this.child_length = 0;
        this.ratatosk = false;
        this.depth = -1;
    }
}

// depth first search function
int dfs(Node node) {

    if (node.ratatosk) return 0;

    Node[] stack = new Node[10];

    stack[0] = node;

    int stack_length = 1;

    node.depth = 0;

    while (stack_length > 0) {
        Node current = stack[stack_length - 1];
        stack_length--;

        for (int i = 0; i < current.child_length; i++) {
            Node child_node = current.child[i];
            if (child_node.ratatosk) return current.depth + 1;
            child_node.depth = current.depth + 1;
            stack_length++;
            stack[stack_length - 1] = child_node;
        }
    }

    return -1;

}

int main() {

    string buf;
    stdin.readln();
    // get range of nodes
    int node_count = to!int(strip(stdin.readln()));

    Node[] nodes = new Node[node_count];

    int nodes_length = 0;

    // create node objects for all nodes
    for (int i = 0; i < node_count; i++) {
        nodes_length++;
        nodes[nodes_length - 1] = new Node();
    }
    // get start node
    Node start_node = nodes[to!int(strip(stdin.readln()))];
    // get goal node
    nodes[to!int(strip(stdin.readln()))].ratatosk = true;

    string[] numbers;
    Node temp_node;

    // add information to node objects
    while(stdin.readln(buf)) {
        numbers = split(buf);
        temp_node = nodes[to!int(numbers[0])];
        numbers = numbers[1..$];
        for (int i = 0; i < numbers.length; i++) {
            temp_node.child_length++;
            temp_node.child[temp_node.child_length - 1] = nodes[to!int(numbers[to!int(i)])];
        }
    }

    /*

    // use switch instead of if-else for better translation, default to dfs
    switch(func) {
        case "dfs":
            writeln(dfs(start_node));
            break;
        case "bfs":
            writeln(bfs(start_node));
            break;
        default:
            writeln(dfs(start_node));
            break;
    }

    */

    writeln(dfs(start_node));

    return 0;

}
