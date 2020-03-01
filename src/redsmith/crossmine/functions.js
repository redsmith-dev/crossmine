/*
    These functions govern the size of the mined cross
    and the activation chance. Edit at will (javascript syntax)
*/

//Example of activation chance linearly dependent on level up to a max of 80%.
//Must return non negative value from 0 to 1, inclusive.
function activation_chance(level, max_level) {
    return (level/max_level) * 0.8;
}

//Example of cross size equal to half the level.
//Must return non negative integer, float values will be rounded to integers.
function cross_size(level, max_level) {
    return level/2;
}