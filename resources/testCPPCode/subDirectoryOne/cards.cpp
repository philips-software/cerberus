#include <cstdio>
#include <cstdint>
using namespace std;

#pragma GCC diagnostic push
#pragma GCC diagnostic ignored "-Wunused-parameter"

enum card_suit : uint8_t { SPD, HRT, DIA, CLB };
enum card_rank : uint8_t { ACE = 1, DEUCE = 2, JACK = 11, QUEEN, KING };

constexpr const char * aceString = "Ace";
constexpr const char * jckString = "Jack";
constexpr const char * queString = "Queen";
constexpr const char * kngString = "King";
constexpr const char * deuString = "Deuce";
constexpr const char * spdString = "Spades";
constexpr const char * hrtString = "Hearts";
constexpr const char * diaString = "Diamonds";
constexpr const char * clbString = "Clubs";

struct card {
    uint8_t rank : 4;
    uint8_t suit : 4;
};

card deck[52] = {
    { ACE, SPD }, { DEUCE, SPD }, { 3, SPD }, { 4, SPD }, { 5, SPD }, { 6, SPD }, { 7, SPD },
    { 8, SPD }, { 9, SPD }, { 10, SPD }, { JACK, SPD }, { QUEEN, SPD }, { KING, SPD },
    { 1, HRT }, { 2, HRT }, { 3, HRT }, { 4, HRT }, { 5, HRT }, { 6, HRT }, { 7, HRT },
    { 8, HRT }, { 9, HRT }, { 10, HRT }, { 11, HRT }, { 12, HRT }, { 13, HRT },
    { 1, DIA }, { 2, DIA }, { 3, DIA }, { 4, DIA }, { 5, DIA }, { 6, DIA }, { 7, DIA },
    { 8, DIA }, { 9, DIA }, { 10, DIA }, { 11, DIA }, { 12, DIA }, { 13, DIA },
    { 1, CLB }, { 2, CLB }, { 3, CLB }, { 4, CLB }, { 5, CLB }, { 6, CLB }, { 7, CLB },
    { 8, CLB }, { 9, CLB }, { 10, CLB }, { 11, CLB }, { 12, CLB }, { 13, CLB }
};

void print_card( const card & c ) {
    if(c.rank > DEUCE && c.rank < JACK) {
        printf("%d of ", c.rank);
    } else {
        switch(c.rank) {
            case ACE:
                printf("%s of ", aceString);
                break;
            case JACK:
                printf("%s of ", jckString);
                break;
            case QUEEN:
                printf("%s of ", queString);
                break;
            case KING:
                printf("%s of ", kngString);
                break;
            case DEUCE:
                printf("%s of ", deuString);
                break;
        }
    }
    switch(c.suit) {
        case SPD:
            puts(spdString);
            break;
        case HRT:
            puts(hrtString);
            break;
        case DIA:
            puts(diaString);
            break;
        case CLB:
            puts(clbString);
            break;
    }
}

int main() {
    long int count = sizeof(deck) / sizeof(card);
    printf("count: %ld cards\n", count);
    for( card c : deck ) {
        print_card(c);
    }
    
    return 0;
}
#pragma GCC diagnostic pop