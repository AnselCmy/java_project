import com.oracle.tools.packager.jnlp.JNLPBundler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BlockMoving extends JFrame {
    BlockGrids blockGrids;
    StartPanel startPanel;
    public BlockMoving() {
        blockGrids = new BlockGrids();
        startPanel = new StartPanel(blockGrids);
        setLayout(new BorderLayout());
//        setSize(300, 300);
        setBounds(200, 200,300, 300);
        getContentPane().add(blockGrids, BorderLayout.CENTER);
        getContentPane().add(startPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new BlockMoving();
//        int []tempBtnNum = new int[]{2,0,1};
//        for(int i: tempBtnNum) {
//            System.out.println(i);
//        }
//        int reverseCnt = 0;
//        for(int i=0; i< tempBtnNum.length; i++) {
//            for(int j=i+1; j<tempBtnNum.length; j++) {
//                if(tempBtnNum[j] < tempBtnNum[i])
//                    reverseCnt++;
//            }
//        }
//        System.out.println(reverseCnt%2 == 0);
    }
}

class StartPanel extends JPanel {
    JButton startBtn;
    public StartPanel(BlockGrids bg) {
        setLayout(new BorderLayout());
        startBtn = new JButton("START");
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bg.RandomStart();
                bg.SetNum();
            }
        });
        add(startBtn, BorderLayout.CENTER);
        setVisible(true);
    }
}

class BlockGrids extends JPanel {
    int num = 3;
    JButton [][]btn = new JButton[num][num];
    int [][]btnNum = new int[num][num];
    public BlockGrids() {
        setLayout(new GridLayout(num, num));
        setVisible(true);
        for(int i=0; i<num; i++) {
            for(int j=0; j<num; j++) {
                btn[i][j] = new JButton("0");
                add(btn[i][j]);
            }
        }
        for(int i=0; i<num; i++) {
            for(int j=0; j<num; j++) {
                btn[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for(int a=0; a<num; a++) {
                            for(int b=0; b<num; b++) {
                                if((JButton)e.getSource() == btn[a][b])
                                    ClickBtn(a, b);
                            }
                        }
                    }
                });
            }
        }
        RandomStart();
        SetNum();
    }

    public void RandomStart() {
        for(int i=0; i<num; i++) {
            for(int j=0; j<num; j++) {
                btnNum[i][j] = i*num+j;
            }
        }
        for(int i=0; i<100; i++) {
            RandomPos();
        }
        while(!CanBeSolved()) {
            RandomPos();
        }
        return;
    }

    public void RandomPos() {
        int x1 = (int) (Math.random() * num);
        int x2 = (int) (Math.random() * num);
        int y1 = (int) (Math.random() * num);
        int y2 = (int) (Math.random() * num);
        int temp = btnNum[x1][y1];
        btnNum[x1][y1] = btnNum[x2][y2];
        btnNum[x2][y2] = temp;
    }

    public void SetNum() {
        for(int i=0; i<num; i++) {
            for(int j=0; j<num; j++) {
                btn[i][j].setText(String.valueOf(btnNum[i][j]));
                if(btnNum[i][j] == num*num-1)
                    btn[i][j].setVisible(false);
                else
                    btn[i][j].setVisible(true);
            }
        }
    }

    public void ClickBtn(int a, int b) {
        if(NearBlank(a, b)) {
            int []blankPos = GetBlankPos();
            int xb = blankPos[0];
            int yb = blankPos[1];
            btnNum[xb][yb] = btnNum[a][b];
            btnNum[a][b] = num*num-1;
            SetNum();
            btn[xb][yb].requestFocus();
            CheckEnd();
        }
    }

    public int[] GetBlankPos() {
        for(int i=0; i<num; i++) {
            for (int j = 0; j < num; j++) {
                if(btnNum[i][j] == num*num-1)
                    return new int[]{i, j};
            }
        }
        return new int[2];
    }

    public ArrayList<Integer> GetArrayList(boolean withEnd) {
        ArrayList<Integer> tempBtnNum = new ArrayList<>();
        for(int i=0; i<num; i++) {
            for(int j=0; j<num; j++) {
                if(!withEnd && btnNum[i][j] == num*num-1) {}
                else
                    tempBtnNum.add(btnNum[i][j]);
            }
        }
        return tempBtnNum;
    }

    public void CheckEnd() {
        boolean isEnd = true;
        ArrayList<Integer> tempBtnNum = GetArrayList(true);
        tempBtnNum.add(num*num-1);
        for(int i=0; i<num*num; i++) {
            if(tempBtnNum.get(i) != i) {
                isEnd = false;
                break;
            }
        }
        if(isEnd) {
            JOptionPane.showMessageDialog(this, "WIN");
        }
    }

    public boolean NearBlank(int a, int b) {
        for(int i: new int[]{a-1, a+1}) {
            if(i>=0 && i<num && btnNum[i][b]==num*num-1) {
                return true;
            }
        }
        for(int i: new int[]{b-1, b+1}) {
            if(i>=0 && i<num && btnNum[a][i]==num*num-1) {
                return true;
            }
        }
        return false;
    }

    public boolean CanBeSolved() {
        ArrayList<Integer> tempBtnNum = GetArrayList(false);
        for(int i: tempBtnNum) {
            System.out.println(i);
        }
        int reverseCnt = 0;
        for(int i=0; i<tempBtnNum.size(); i++) {
            for(int j=i+1; j<tempBtnNum.size(); j++) {
                if(tempBtnNum.get(j) < tempBtnNum.get(i))
                    reverseCnt++;
            }
        }
        return (reverseCnt%2 == 0);
    }
}
