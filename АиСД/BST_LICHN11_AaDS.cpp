#include <iostream>
#include <fstream>
#include <climits>

using namespace std;

struct BST
{
    int data;
    BST* left;
    BST* right;
    int hight;

    BST(int value) 
    {
        data = value;
        left = NULL;
        right = NULL;
        hight = 0;
    }
};

void insert(BST*& root, int element)
{
    if (root == NULL)
    {
        root = new BST(element);
        return;
    }
    if (root->data < element)
    {
        insert(root->right, element);
    }
    else if (root->data > element)
    {
        insert(root->left, element);
    }
}

int setheights(BST*& root)
{
    if (root == NULL) {
        return -1;
    }
    int leftheigt = setheights(root->left);
    int rightheight = setheights(root->right);

    root->hight = max(leftheigt, rightheight) + 1;
    return root->hight;
}

int findMaxDiameter(BST* root)
{
    if (root == NULL)
    {
        return 0;
    }

    int leftHeight = -1;
    int rightHeight = -1;

    if (root->left != NULL)
    {
        leftHeight = root->left->hight;
    }

    if (root->right != NULL)
    {
        rightHeight = root->right->hight;
    }

    int currentDiameter = leftHeight + rightHeight + 2;

    int leftDiameter = findMaxDiameter(root->left);
    int rightDiameter = findMaxDiameter(root->right);

    int max1 = max(leftDiameter, rightDiameter);
    return max(currentDiameter, max1);
}

int poiskminleafdiam(BST* root, int maxdiam)
{
    if (root == NULL) {
        return -1;
    }

    int leftResult = poiskminleafdiam(root->left, maxdiam);
    if (leftResult != -1)
    {
        return leftResult;
    }

    int leftHeight = -1;
    int rightHeight = -1;

    if (root->left != NULL) 
    {
        leftHeight = root->left->hight;
    }

    if (root->right != NULL) 
    {
        rightHeight = root->right->hight;
    }

    int curdiam = leftHeight + rightHeight + 2;

    if (curdiam == maxdiam)
    {
        return root->data;
    }
    int RightResult = poiskminleafdiam(root->right, maxdiam);
    if (RightResult != -1)
    {
        return RightResult;
    }
    return -1;
}
void leviprobxod(BST* root, ofstream& out) {
    if (root == NULL) {
        return;
    }
    out << root->data << endl;
    leviprobxod(root->left, out);
    leviprobxod(root->right, out);
}

void pravoedelete(BST*& root, int del)
{
    if (root == NULL)
    {
        return;
    }
    if (del < root->data)
    {
        pravoedelete(root->left, del);
    }
    else if (del > root->data)
    {
        pravoedelete(root->right, del);
    }
    else if (del == root->data)
    {
        if (root->left == NULL && root->right == NULL)
        {
            delete root;
            root = NULL;
        }
        else if (root->left == NULL)
        {
            BST* temp = root;
            root = root->right;
            delete temp;
        }
        else if (root->right == NULL)
        {
            BST* temp = root;
            root = root->left;
            delete temp;
        }
        else
        {
            BST* nash = root->right;
            BST* pred = root;

            while (nash->left != NULL)
            {
                pred = nash;
                nash = nash->left;
            }

            root->data = nash->data;

            if (pred == root)
            {
                pred->right = nash->right;
            }
            else
            {
                pred->left = nash->right;
            }
            delete nash;
        }
    }
    return;
}

int main() {
    ifstream in("input.txt");
    ofstream out("output.txt");

    BST* root = NULL;
    int element;
    in >> element;
    root = new BST(element);
    while (in >> element) {
        insert(root, element);
    }


    setheights(root);
    int maxdiam = findMaxDiameter(root);
    int res = -1;
    res = poiskminleafdiam(root, maxdiam);
    pravoedelete(root, res);
    leviprobxod(root, out);



    in.close();
    out.close();

    return 0;
}