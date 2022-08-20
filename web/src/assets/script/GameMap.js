import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
    constructor(ctx, parent) {  // ctx表示画布， parent表示父元素
        super();

        this.ctx = ctx;
        this.parent = parent;

        this.L = 0; // 每一个格子绝对距离

        this.rows = 50;
        this.clos = 50;

        this.inner_wall_count = 220;
        this.walls = [];  // 用于存储所有的墙
    }

    check_connectivity (g, sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for (let i = 0; i < 4; i ++) {
            let x = sx + dx[i], y = sy + dy[i];
            if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty))
                return true;
        }
        return false;
    }

    create_walls () {
         const g = [];
         for (let r = 0; r < this.rows; r ++) {
            g[r] = [];
            for (let c = 0; c < this.clos; c ++) {
                g[r][c] = false;
            }
         }
        
         // 创建外围障碍物
         for (let r = 0; r < this.rows; r ++) {
            g[r][0] = true;
            g[r][this.clos - 1] = true;
            g[0][r] = true;
            g[this.clos - 1][r] = true;
         }

         // 创建随机障碍物
         for (let i = 0; i < this.inner_wall_count / 2; i ++) {
            for (let j = 0; j < 1000; j ++) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.clos);
                if (g[r][c] || g[c][r]) continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && this.clos - 2) continue;
                g[r][c] = g[c][r] = true;
                break;
            }
         }
        
         // 判断是否连通
         const copy_g = JSON.parse(JSON.stringify(g));
         if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.clos - 2)) 
            return false;

         for (let r = 0; r < this.rows; r ++) {
            for (let c = 0; c < this.clos; c ++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c ,this));
                }
            }
         }

         return true;
    }

    start () {  // 只执行一次
        for (let i = 0; i < 1000; i ++)
            if (this.create_walls())
                break;
    }

    update_size () {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.clos, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.clos;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update () {  // 每一帧执行一次
        this.update_size();
        this.render();  // 每一帧执行一次
    }

    render () {  // 渲染
        //#A2D048
        //#AAD752
        const color_even = "#D6CDC4", color_odd = "#CCBCAA";
        for (let r = 0; r < this.rows; r ++) {
            for (let c = 0; c < this.clos; c ++) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }
}