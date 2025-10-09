#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>

using namespace std;

int main()
{
    int size1 = 0;
    int ch = 0;
    cin >> size1;

    int* s1 = new int[size1];
    int* s2 = new int[size1];

    for (int i = 0; i < size1; i++)
    {
        cin >> ch;
        s1[i] = ch;
    }
    for (int i = 0; i < size1; i++)
    {
        cin >> ch;
        s2[i] = ch;
    }

    int** dp = new int* [size1 + 1];
    for (int i = 0; i < size1 + 1; i++)
    {
        dp[i] = new int[size1 + 1];
    }

    for (int i = 0; i < size1 + 1; i++)
    {
        dp[i][0] = 0;
        dp[0][i] = 0;
    }

    for (int i = 1; i < size1 + 1; i++)
    {
        for (int j = 1; j < size1 + 1; j++)
        {
            if (s1[i - 1] == s2[j - 1])  
            {
                dp[i][j] = dp[i - 1][j - 1] + 1;
            }
            else  
            {
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
    }
    cout << dp[size1][size1]<<endl;
    vector<int> path = {};
    vector<int> path2 = {};
    int i = size1, j = size1;
    while (i > 0 && j > 0)  
    {
        if (s1[i - 1] == s2[j - 1])  
        {
            path.push_back(i-1);  
            path2.push_back(j - 1);
            i=i-1;
            j=j-1;
        }
        if (s1[i - 1] != s2[j - 1])
        {
            if (dp[i][j] == dp[i][j-1])
            {
                j = j - 1;
            }
            else
            {
                i = i - 1;
            }         
        }
  
    }
    reverse(path.begin(), path.end());
    reverse(path2.begin(), path2.end());
    for (int i = 0; i < path.size(); i++)
    {
        cout << path[i]<< " ";  
    }
    cout << endl;
    for (int j = 0; j < path2.size(); j++)
    {
        cout << path2[j] << " ";
    }
    delete[] s1;
    delete[] s2;
    for (int i = 0; i < size1 + 1; i++) {
        delete[] dp[i];
    }
    delete[] dp;

    return 0;
}