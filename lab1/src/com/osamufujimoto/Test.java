package com.osamufujimoto;

import static com.osamufujimoto.Util.LOGD;

public class Test {

    public static  void testBrownPath(Node n) {

        if (n.x == 246 || n.x == 288 || n.x == 282 || n.x == 290 || n.x == 304 || n.x == 306) {

            assert(n.t == Terrain.EASY_MOVEMENT_FOREST);

            LOGD(n.toString() + " is easy movement forest");
        }

        if (n.x == 243) {

            assert(n.t == Terrain.FOOTPATH);

            LOGD(n.toString() + " is footpath");

        }

        if (n.x == 253) {

            assert(n.t == Terrain.SLOW_RUN_FOREST);

            LOGD(n.toString() + " is slow run forest");

        }

        if (n.x == 276) {

            assert(n.t == Terrain.WALK_FOREST);

            LOGD(n.toString() + " is walk forest");


        }

        if (n.x == 303) {

            assert(n.t == Terrain.OPEN_LAND);

            LOGD(n.toString() + " is open land");


        }

    }
}
