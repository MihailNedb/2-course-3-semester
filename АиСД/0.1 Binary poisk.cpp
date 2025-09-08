#include <iostream>
#include <vector>
using namespace std;

int Mybinarysearch(const vector<long long int>& mas, long long int x) {
    if (mas.empty()) return -1;

    int l = 0;
    int r = mas.size() - 1;
    int result = -1;

    while (r >= l) {
        int mid = l + (r - l) / 2;
        if (mas[mid] == x) {
            result = mid;
            r = mid - 1;
        }
        else if (mas[mid] > x) {
            r = mid - 1;
        }
        else {
            l = mid + 1;
        }
    }
    return result;
}

int Mybinarybolche(const vector<long long int>& mas, long long int x) {
    int l = 0;
    int r = mas.size();

    while (l < r) {
        int mid = l + (r - l) / 2;
        if (mas[mid] < x) {
            l = mid + 1;
        }
        else {
            r = mid;
        }
    }
    return l;
}

int main()
{
    ios_base::sync_with_stdio(false);
    cin.tie(0);

    vector<long long int> mas;

    long long size;
    cin >> size;

    for (long long i = 0; i < size; i++) {
        long long num;
        cin >> num;
        mas.push_back(num);
    }

    long long csize;
    cin >> csize;

    for (long long i = 0; i < csize; i++) {
        long long num;
        cin >> num;

        int first = Mybinarysearch(mas, num);

        if (first != -1) {
            cout << 1 << " ";
            cout << first << " ";
            cout << Mybinarybolche(mas, num + 1) << "\n";
        }
        else {
            cout << 0 << " ";
            long long l1 = Mybinarybolche(mas, num);
            cout << l1 << " ";
            cout << l1 << "\n";
        }
    }

    return 0;
}