#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

void domsum(vector<vector<int>>& vec, int& count, vector<bool>& visited, int j1)
{
    vector<int> cur;
    visited[j1] = true;
    count = 0;

    for (int j = 1; j < vec[j1].size(); j++)
    {
        count = 1;
        int n = vec[j1][j];
        if (visited[n] == false)
        {
            visited[n] = true;
            cur.push_back(n);
        }
    }

    while (!cur.empty())
    {
        vector<int> next;

        for (int i = 0; i < cur.size(); i++)
        {
            int current = cur[i];

            for (int j = 1; j < vec[current].size(); j++)
            {
                int n = vec[current][j];
                if (visited[n] == false)
                {
                    visited[n] = true;
                    next.push_back(n);
                }
            }
        }

        if (next.empty() == false)
        {
            count++;
        }

        cur = next;
    }

    for (int i = 0; i < visited.size(); i++)
    {
        if (visited[i] == false)
        {
            count = -1;
            return;
        }
    }
}

int main()
{
    ifstream in("input.txt");
    ofstream out("output.txt");

    int size1;
    in >> size1;
    vector<vector<int>> vec(size1);

    for (int i = 0; i < size1; i++)
    {
        int x;
        in >> x;
        vec[i].push_back(i);
        for (int j = 0; j < x; j++)
        {
            int a;
            in >> a;
            vec[i].push_back(a - 1);
        }
    }

    int maxcount = -1;
    int maxstart = 0;

    for (int j = 0; j < size1; j++)
    {
        vector<bool> visited(size1, false);
        int count = 0;
        domsum(vec, count, visited, j);


        if (count != -1 && count >= maxcount)
        {
            maxcount = count;
            maxstart = j;
        }
    }

    if (maxcount == -1)
    {
        out << "impossible";
    }
    else
    {
        out << maxcount << endl;
        out << maxstart + 1 << endl;
    }

    in.close();
    out.close();
}