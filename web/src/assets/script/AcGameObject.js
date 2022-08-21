const AC_GAME_OBJECTS = [];  // 存储所有游戏变量

export class AcGameObject {  // 定义基类
    constructor() {  // 构造函数 
        AC_GAME_OBJECTS.push(this);
        this.timedelta = 0;  // 每一帧消耗的时间
        this.has_called_start = false;
    }

    start() {  // 只执行一次
        
    }

    update() {  // 除了第一帧之外，每一帧执行一次

    }

    on_destroy() {  // 删除之前执行

    }

    destroy() {  // 删除相同的
        this.on_destroy();

        for (let i in AC_GAME_OBJECTS) {
            const obj = AC_GAME_OBJECTS[i];
            if (obj == this) {
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}

let last_timestamp;  // 上一次执行的时刻
const step = timestamp => {
    for (let obj of AC_GAME_OBJECTS) {
        if (!obj.has_called_start) {
            obj.has_called_start = true;
            obj.start();
        } else {
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    requestAnimationFrame(step)
}

requestAnimationFrame(step)  // 在下一帧渲染之前执行一次step函数