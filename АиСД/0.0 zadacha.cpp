

#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

using namespace std;

int main()
{
	ifstream input1("input.txt");

	ofstream output1("output.txt");

	vector<long long int> vec;

	long long int n;

	long long int sum=0;

	while (input1 >> n)
	{
		vec.push_back(n);
	}

	sort(vec.begin(), vec.end());

	

	for (int i = 0; i < vec.size(); i++)
	{
		if (i == 0 || vec[i] != vec[i-1])
		{
			sum = sum + vec[i];
		}
		
	}
	output1 << sum;

	input1.close();

	output1.close();

	return 0;
}


