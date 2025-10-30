

#include <iostream>
#include <fstream>

using namespace std;
void rec(int* m,int size1,bool& check1,int& i)
{
	if (check1 == true)
	{
		if (2 * i + 1 <= size1-1)
		{
			if (m[i] > m[2 * i + 1])
			{
				check1 = false;
				return;
			}
			int newi = 2 * i + 1;
			rec(m, size1, check1, newi);
		}
		if (2 * i + 2 <= size1-1)
		{
			if (m[i] > m[2 * i + 2])
			{
				check1 = false;
				return;
			}
			int newi = 2 * i + 2;
			rec(m, size1, check1, newi);
		}
	}
	return;
}
int main()
{
	ifstream in("input.txt");
	ofstream out("output.txt");

	int size1 = 0;
	in >> size1;
	int* m = new int[size1];
	for (int i = 0; i < size1; i++)
	{
		in >> m[i];
	}
	int i = 0;
	bool check1 = true;
	rec(m, size1, check1, i);
	cout << check1;
	if (check1 == true)
	{
		out << "Yes";
	}
	else
	{
		out << "No";
	}

	in.close();
	out.close();
}

