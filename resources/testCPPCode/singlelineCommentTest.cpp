#include <cstdio>
using namespace std;

//#pragma warning( disable : 4100 )
class S {
public:
    int static_value() {
        static int x = 7;
        return ++x;
    }
};

int func() {
    int x = 7;
    return ++x;
}

int main() {
    int i = 42;
    printf("The integer is %d\n", i);
    return 0;
}
