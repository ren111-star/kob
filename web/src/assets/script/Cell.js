export class Cell {
    constructor(r, c) {
        this.r = r;
        this.c = c;
        // 画布的坐标轴不同于数组
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}