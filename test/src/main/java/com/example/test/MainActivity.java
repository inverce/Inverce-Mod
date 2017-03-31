package com.example.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;
import com.inverce.mod.core.collections.TreeNode;
import com.inverce.mod.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IMInitializer.initialize(this);
        setContentView(R.layout.activity_main);

        List<IntNode> all = new ArrayList<>();
        IntNode root = createTree(new IntNode(1), all, 2, 5);

        final Handler handler = new Handler(Looper.getMainLooper());

        Event<Intre> test = new Event<>(Intre.class);

        test.addListener(new IntreImpl());
        test.addListener(() -> Log.w("pp2"));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.w("d");
                for (int i=0;i<1; i++) {
                    IM.onBg().schedule(() -> {
                        test.post().pp();
                        System.gc();
                    }, 1500, TimeUnit.MILLISECONDS);
                }
                handler.postDelayed(this, 2000);
            }
        }, 2000);

    }

    private IntNode createTree(IntNode node, List<IntNode> all, int level, int size) {
        for (int i = 0; i < size; i++) {
            IntNode inNode = new IntNode(node.value * 10 + i);
            all.add(inNode);
            if (level > 0) {
                createTree(inNode, all, level - 1, size);
            }
            node.getChildren().add(inNode);
        }
        return node;
    }

    static class IntreImpl implements Intre {
        @Override
        public void pp() {
            Log.w("PP1");
        }
    }

    class IntNode extends TreeNode<IntNode> {
        int value;

        public IntNode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "IntNode{" + value + '}';
        }
    }
}
