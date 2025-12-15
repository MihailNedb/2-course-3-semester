#include <iostream>
#include <fstream>
#include <vector>
#include <utility>
#include <queue>
#include <climits>

using namespace std;

int Dijkstra(int end, const vector<vector<pair<int, int>>>& graph) {
    size_t n = graph.size();
    vector<int> dist(n, LONG_MAX);
    vector<bool> visited(n, false);


    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;

    dist[0] = 0; 
    pq.push({0, 0});

    while (!pq.empty()) {
        int curdist = pq.top().first;
        int cur3 = pq.top().second;
        pq.pop();

        if (visited[cur3])
        {
            continue;
        }

        visited[cur3] = true;
      
        if (cur3 == end - 1) {
            return curdist;
        }

        for (const auto& edge : graph[cur3]) {
            int neighbor = edge.first - 1; 
            int weight = edge.second;

            if (!visited[neighbor]) {
                int new_dist = curdist + weight;
                if (new_dist < dist[neighbor]) {
                    dist[neighbor] = new_dist;
                    pq.push({ new_dist, neighbor });
                }
            }
        }
    }

    return dist[end - 1];
}

int main() {
    int sizev;  
    int sizer; 
    ifstream in("input.txt");
    ofstream out("output.txt");

    in >> sizev;
    in >> sizer;
    if (sizev == 0 && sizer == 0)
    {
        out << 0; 
        return 0;
    }
    vector<vector<pair<int, int>>> spisok(sizev);

    int cur = 0;    
    int cur2 = 0;   
    int weight = 0; 

    for (int i = 0; i < sizer; i++) {
        in >> cur;
        in >> cur2;
        in >> weight;

        spisok[cur - 1].push_back(make_pair(cur2, weight));
        spisok[cur2 - 1].push_back(make_pair(cur, weight));
    }

    int min_distance = Dijkstra(sizev, spisok);

    out << min_distance << endl;

    in.close();
    out.close();
}