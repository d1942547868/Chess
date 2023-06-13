

import java.awt.*;//Font类
import java.awt.event.*;
import javax.swing.*;//JFrame



//主框架类
public class ChessMainFrame extends JFrame implements ActionListener,MouseListener,Runnable{//继承JFrame，构造方法就是一个窗体，无须再进行实例化
    //棋子
    JLabel play[] = new JLabel[32];
    //棋盘
    JLabel checkerboard;
    //窗格
    Container pane;
    //工具栏
    JToolBar toolbars;
    //重新开始
    JButton restart;
    //退出
    JButton exit;
    //当前信息
    JLabel text;

    //规则类对象(使于调用方法)
    ChessRule rule;
    /**
     ** 单击棋子
     ** chessManClick = true 闪烁棋子 并给线程响应
     ** chessManClick = false 吃棋子 停止闪烁  并给线程响应
     */
    boolean chessManClick;
    /**
     ** 控制玩家走棋
     ** chessPlayClick=1 黑棋走棋
     ** chessPlayClick=2 红棋走棋 默认红棋
     ** chessPlayClick=3 双方都不能走棋
     */
    int chessPlayClick=2;//红二黑一
    //控制棋子闪烁的线程
    Thread tmain;//线程变量
    //把第一次的单击棋子给线程响应
    static int Man,i;

    ChessMainFrame(){
        new ChessMainFrame("中国象棋");
    }

    /**
     ** 构造函数
     ** 初始化图形用户界面
     */
    public ChessMainFrame(String Title){
        //改变系统默认字体
        Font font = new Font("宋体", Font.PLAIN, 12);//（宋体,普通，12pt）
        java.util.Enumeration keys = UIManager.getDefaults().keys();//得到一个枚举对象keys，其中包含所有的属性键
        while (keys.hasMoreElements()) {//使用keys.hasMoreElements()方法检查枚举对象中是否还有下一个元素，如果有则使用
            Object key = keys.nextElement();//keys.nextElement()方法获得下一个键，并赋值给Object类型的key变量
            Object value = UIManager.get(key);//UIManager.get(key)方法获取指定键的界面属性值，即获取该属性键对应的UIManager的值，保存在Object类型中的value中
            if (value instanceof javax.swing.plaf.FontUIResource) {//判断获取到的值是否是 FontUIResource 类型，如果是，则表明这个属性键是字体相关的属性键。FontUIResource 是 Swing 中一个专门用于表示字体的类。
                UIManager.put(key, font);//替换为新字体 font。这是通过调用UIManager.put(key, font)方法实现的。
            }
        }
        //得到窗口的内容面板
        pane = this.getContentPane();//得到窗口的内容面板
        pane.setLayout(null);//绝对布局，防止组界面的位置和形状随着窗口的变化而变化
        //实例化规则类
        rule = new ChessRule();

        //创建工具栏
        toolbars = new JToolBar();
        text = new JLabel("欢迎使用象棋对弈系统");
        //当鼠标放上显示信息
        text.setToolTipText("信息提示");//setToolTip~鼠标悬停出现的内容
        restart = new JButton(" 新 游 戏 ");
        restart.setToolTipText("重新开始新的一局");
        exit = new JButton(" 退  出 ");
        exit.setToolTipText("退出象棋程序程序");


        //把组件添加到工具栏
        toolbars.setLayout(new GridLayout(1,3));//工具栏采用三格布局，1排分三格
        toolbars.add(restart);
        toolbars.add(exit);
        toolbars.add(text);
        toolbars.setBounds(0,0,558,30);//设置像素,组件左上角在容器的坐标和组件的大小
        pane.add(toolbars);//将工具栏添加到内容面板

        //添加棋子标签（调用绝对布局以后，先添加的在上方，所以需要先添加棋子再添加棋盘）
        drawChessMan();//调用方法

        //注册按扭监听
        restart.addActionListener(this);
        exit.addActionListener(this);

        //注册棋子移动监听
        for (int i=0;i<32;i++){
            pane.add(play[i]);
            play[i].addMouseListener(this);
        }

        //添加棋盘标签
        pane.add(checkerboard = new JLabel(new ImageIcon("E:\\idea java\\chess\\src\\image\\QQ图片20230612203821.jpg")));
        checkerboard.setBounds(0,30,558,620);
        checkerboard.addMouseListener(this);


        //窗体居中
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        

        this.setLocation((screenSize.width - frameSize.width) / 2 - 280 ,(screenSize.height - frameSize.height ) / 2 - 350);

        //设置
        this.setIconImage(new ImageIcon("E:\\idea java\\chess\\src\\image\\QQ图片20230612203206.jpg").getImage());//给程序一个小图标
        this.setResizable(false);//让窗体不能改变大小
        this.setTitle(Title);
        this.setSize(558,670);//当前窗口大小,棋盘620，工具栏30，标题栏20
        this.setVisible(true);//当前窗口可见
    }

    /**
     ** 添加棋子方法
     */
    public void drawChessMan(){
        //流程控制
        int i,k;
        //图标
        Icon in;

        //黑色棋子

        //车
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\黑车.gif");
        for (i=0,k=24;i<2;i++,k+=456){
            play[i] = new JLabel(in);
            play[i].setBounds(k,56,55,55);
            play[i].setName("车1");
        }

        //马
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\黑马.gif");
        for (i=4,k=81;i<6;i++,k+=342){
            play[i] = new JLabel(in);
            play[i].setBounds(k,56,55,55);//定位
            play[i].setName("马1");
        }

        //相
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\黑象.gif");
        for (i=8,k=138;i<10;i++,k+=228){
            play[i] = new JLabel(in);
            play[i].setBounds(k,56,55,55);
            play[i].setName("象1");
        }

        //士
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\黑士.gif");
        for (i=12,k=195;i<14;i++,k+=114){
            play[i] = new JLabel(in);
            play[i].setBounds(k,56,55,55);
            play[i].setName("士1");
        }

        //卒
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\黑卒.gif");
        for (i=16,k=24;i<21;i++,k+=114){
            play[i] = new JLabel(in);
            play[i].setBounds(k,227,55,55);
            play[i].setName("卒1" + i);
        }

        //炮
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\黑炮.gif");
        for (i=26,k=81;i<28;i++,k+=342){
            play[i] = new JLabel(in);
            play[i].setBounds(k,170,55,55);
            play[i].setName("炮1" + i);
        }

        //将
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\黑将.gif");
        play[30] = new JLabel(in);
        play[30].setBounds(252,56,55,55);
        play[30].setName("将1");

        //红色棋子
        //车
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\红车.GIF");
        for (i=2,k=24;i<4;i++,k+=456){
            play[i] = new JLabel(in);
            play[i].setBounds(k,569,55,55);
            play[i].setName("车2");
        }

        //马
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\红马.gif");
        for (i=6,k=81;i<8;i++,k+=342){
            play[i] = new JLabel(in);
            play[i].setBounds(k,569,55,55);
            play[i].setName("马2");
        }

        //相
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\红象.gif");
        for (i=10,k=138;i<12;i++,k+=228){
            play[i] = new JLabel(in);
            play[i].setBounds(k,569,55,55);
            play[i].setName("象2");
        }

        //士
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\红士.gif");
        for (i=14,k=195;i<16;i++,k+=114){
            play[i] = new JLabel(in);
            play[i].setBounds(k,569,55,55);
            play[i].setName("士2");
        }

        //兵
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\红卒.gif");
        for (i=21,k=24;i<26;i++,k+=114){
            play[i] = new JLabel(in);
            play[i].setBounds(k,398,55,55);
            play[i].setName("卒2" + i);
        }

        //炮
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\红炮.gif");
        for (i=28,k=81;i<30;i++,k+=342){
            play[i] = new JLabel(in);
            play[i].setBounds(k,455,55,55);
            play[i].setName("炮2" + i);
        }

        //帅
        in = new ImageIcon("E:\\idea java\\chess\\src\\image\\红将.gif");
        play[31] = new JLabel(in);
        play[31].setBounds(252,569,55,55);
        play[31].setName("帅2");
    }

    /**
     ** 线程方法控制棋子闪烁
     */
    public void run(){
        while (true){
            //单击棋子第一下开始闪烁
            if (chessManClick){
                play[Man].setVisible(false);//设置不可见

                //时间控制
                try{
                    tmain.sleep(200);
                }
                catch(Exception e){
                }

                play[Man].setVisible(true);//可见
            }

            //闪烁当前提示信息 以免用户看不见
            else {
                text.setVisible(false);

                //时间控制
                try{
                    tmain.sleep(250);
                }
                catch(Exception e){
                }

                text.setVisible(true);
            }

            try{
                tmain.sleep(350);
            }
            catch (Exception e){
            }
        }
    }//

    /**
     ** 单击棋子方法
     */
    public void mouseClicked(MouseEvent me){
      //  System.out.println("Mouse");

        //当前坐标
        int Ex=0,Ey=0;

        //启动线程
        if (tmain == null){
            tmain = new Thread(this);
            tmain.start();
        }

        //单击棋盘(移动棋子)
        if (me.getSource().equals(checkerboard)){//判断点击的为棋盘
            //该红棋走棋的时候
            if (chessPlayClick == 2 && play[Man].getName().charAt(1) == '2'){
                Ex = play[Man].getX();
                Ey = play[Man].getY();
                //移动卒、兵
                if (Man > 15 && Man < 26){
                    rule.armsRule(Man, play[Man],me);
                }

                //移动炮
                else if (Man > 25 && Man < 30){
                    rule.cannonRule(play[Man], play,me);
                }

                //移动车
                else if (Man >=0 && Man < 4){
                    rule.cannonRule(play[Man], play,me);
                }

                //移动马
                else if (Man > 3 && Man < 8){
                    rule.horseRule(play[Man], play,me);
                }

                //移动相、象
                else if (Man > 7 && Man < 12){
                    rule.elephantRule(Man, play[Man], play,me);
                }

                //移动仕、士
                else if (Man > 11 && Man < 16){
                    rule.chapRule(Man, play[Man], play,me);
                }

                //移动将、帅
                else if (Man == 30 || Man == 31){
                    rule.willRule(Man, play[Man], play,me);
                }

                //是否走棋错误(是否在原地没有动)
                if (Ex == play[Man].getX() && Ey == play[Man].getY()){
                    text.setText("               红棋走棋");
                    chessPlayClick=2;
                }

                else {
                    text.setText("               黑棋走棋");
                    chessPlayClick=1;
                }

            }//if

            //该黑棋走棋的时候
            else if (chessPlayClick == 1 && play[Man].getName().charAt(1) == '1'){
                Ex = play[Man].getX();
                Ey = play[Man].getY();

                //移动卒、兵
                if (Man > 15 && Man < 26){
                    rule.armsRule(Man, play[Man],me);
                }

                //移动炮
                else if (Man > 25 && Man < 30){
                    rule.cannonRule(play[Man], play,me);
                }

                //移动车
                else if (Man >=0 && Man < 4){
                    rule.cannonRule(play[Man], play,me);
                }

                //移动马
                else if (Man > 3 && Man < 8){
                    rule.horseRule(play[Man], play,me);
                }

                //移动相、象
                else if (Man > 7 && Man < 12){
                    rule.elephantRule(Man, play[Man], play,me);
                }

                //移动仕、士
                else if (Man > 11 && Man < 16){
                    rule.chapRule(Man, play[Man], play,me);
                }

                //移动将、帅
                else if (Man == 30 || Man == 31){
                    rule.willRule(Man, play[Man], play,me);
                }

                //是否走棋错误(是否在原地没有动)
                if (Ex == play[Man].getX() && Ey == play[Man].getY()){
                    text.setText("               黑棋走棋");
                    chessPlayClick=1;
                }

                else {
                    text.setText("               红棋走棋");
                    chessPlayClick=2;
                }


            }//else if

            //当前没有操作(停止闪烁)
            chessManClick=false;

        }//if

        //单击棋子
        else{
            //第一次单击棋子(闪烁棋子)
            if (!chessManClick){
                for (int i=0;i<32;i++){
                    //被单击的棋子
                    if (me.getSource().equals(play[i])){//判断点击的为棋子
                        //告诉线程让该棋子闪烁
                        Man=i;
                        //开始闪烁
                        chessManClick=true;
                        break;
                    }
                }//for
            }//if

            //第二次单击棋子(吃棋子)
            else if (chessManClick){
                //当前没有操作(停止闪烁)
                chessManClick=false;

                for (i=0;i<32;i++){
                    //找到被吃的棋子
                    if (me.getSource().equals(play[i])){
                        //该红棋吃棋的时候
                        if (chessPlayClick == 2 && play[Man].getName().charAt(1) == '2'){
                            Ex = play[Man].getX();
                            Ey = play[Man].getY();

                            //卒、兵吃规则
                            if (Man > 15 && Man < 26){
                                rule.armsRule(play[Man], play[i]);
                            }

                            //炮吃规则
                            else if (Man > 25 && Man < 30){
                                rule.cannonRule(0, play[Man], play[i], play,me);
                            }

                            //车吃规则
                            else if (Man >=0 && Man < 4){
                                rule.cannonRule(1, play[Man], play[i], play,me);
                            }

                            //马吃规则
                            else if (Man > 3 && Man < 8){
                                rule.horseRule(play[Man], play[i], play,me);
                            }

                            //相、象吃规则
                            else if (Man > 7 && Man < 12){
                                rule.elephantRule(play[Man], play[i], play);
                            }

                            //士、仕吃棋规则
                            else if (Man > 11 && Man < 16){
                                rule.chapRule(Man, play[Man], play[i], play);
                            }

                            //将、帅吃棋规则
                            else if (Man == 30 || Man == 31){
                                rule.willRule(Man, play[Man], play[i], play);
                                play[Man].setVisible(true);
                            }

                            //是否走棋错误(是否在原地没有动)
                            if (Ex == play[Man].getX() && Ey == play[Man].getY()){
                                text.setText("               红棋走棋");
                                chessPlayClick=2;
                                break;
                            }

                            else{
                                text.setText("               黑棋走棋");
                                chessPlayClick=1;
                                break;
                            }

                        }//if

                        //该黑棋吃棋的时候
                        else if (chessPlayClick == 1 && play[Man].getName().charAt(1) == '1'){
                            Ex = play[Man].getX();
                            Ey = play[Man].getY();

                            //卒吃规则
                            if (Man > 15 && Man < 26){
                                rule.armsRule(play[Man], play[i]);
                            }

                            //炮吃规则
                            else if (Man > 25 && Man < 30){
                                rule.cannonRule(0, play[Man], play[i], play,me);
                            }

                            //车吃规则
                            else if (Man >=0 && Man < 4){
                                rule.cannonRule(1, play[Man], play[i], play,me);
                            }

                            //马吃规则
                            else if (Man > 3 && Man < 8){
                                rule.horseRule(play[Man], play[i], play,me);
                            }

                            //相、象吃规则
                            else if (Man > 7 && Man < 12){
                                rule.elephantRule(play[Man], play[i], play);
                            }

                            //士、仕吃棋规则
                            else if (Man > 11 && Man < 16){
                                rule.chapRule(Man, play[Man], play[i], play);
                            }

                            //将、帅吃棋规则
                            else if (Man == 30 || Man == 31){
                                rule.willRule(Man, play[Man], play[i], play);
                                play[Man].setVisible(true);
                            }

                            //是否走棋错误(是否在原地没有动)
                            if (Ex == play[Man].getX() && Ey == play[Man].getY()){
                                text.setText("               黑棋走棋");
                                chessPlayClick=1;
                                break;
                            }

                            else {
                                text.setText("               红棋走棋");
                                chessPlayClick=2;
                                break;
                            }

                        }//else if

                    }//if

                }//for


                //是否胜利
                if (!play[31].isVisible()){
                    JOptionPane.showConfirmDialog(
                            this,"黑棋胜利","玩家一胜利",
                            JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                    //双方都不可以在走棋了
                    chessPlayClick=3;
                    text.setText("  黑棋胜利");

                }//if

                else if (!play[30].isVisible()){
                    JOptionPane.showConfirmDialog(
                            this,"红棋胜利","玩家二胜利",
                            JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                    chessPlayClick=3;
                    text.setText("  红棋胜利");
                }//else if

            }//else

        }//else

    }

    public void mousePressed(MouseEvent me){
    }
    public void mouseReleased(MouseEvent me){
    }
    public void mouseEntered(MouseEvent me){
    }
    public void mouseExited(MouseEvent me){
    }

    /**
     ** 定义按钮的事件响应
     */
    public void actionPerformed(ActionEvent ae) {
        //重新开始按钮
        if (ae.getSource().equals(restart)){
            int i,k;
            //重新排列每个棋子的位置
            //黑色棋子

            //车
            for (i=0,k=24;i<2;i++,k+=456){
                play[i].setBounds(k,56,55,55);
            }

            //马
            for (i=4,k=81;i<6;i++,k+=342){
                play[i].setBounds(k,56,55,55);
            }

            //相
            for (i=8,k=138;i<10;i++,k+=228){
                play[i].setBounds(k,56,55,55);
            }

            //士
            for (i=12,k=195;i<14;i++,k+=114){
                play[i].setBounds(k,56,55,55);
            }

            //卒
            for (i=16,k=24;i<21;i++,k+=114){
                play[i].setBounds(k,227,55,55);
            }

            //炮
            for (i=26,k=81;i<28;i++,k+=342){
                play[i].setBounds(k,170,55,55);
            }

            //将
            play[30].setBounds(252,56,55,55);

            //红色棋子
            //车
            for (i=2,k=24;i<4;i++,k+=456){
                play[i].setBounds(k,569,55,55);
            }

            //马
            for (i=6,k=81;i<8;i++,k+=342){
                play[i].setBounds(k,569,55,55);
            }

            //相
            for (i=10,k=138;i<12;i++,k+=228){
                play[i].setBounds(k,569,55,55);
            }

            //士
            for (i=14,k=195;i<16;i++,k+=114){
                play[i].setBounds(k,569,55,55);
            }

            //兵
            for (i=21,k=24;i<26;i++,k+=114){
                play[i].setBounds(k,398,55,55);
            }

            //炮
            for (i=28,k=81;i<30;i++,k+=342){
                play[i].setBounds(k,455,55,55);
            }

            //帅
            play[31].setBounds(252,569,55,55);

            chessPlayClick = 2;
            text.setText("               红棋走棋");

            for (i=0;i<32;i++){
                play[i].setVisible(true);
            }
            //清除Vector中的内容
            //Save.clear();

        }
        //退出
        else if (ae.getSource().equals(exit)){
            int j=JOptionPane.showConfirmDialog(
                    this,"真的要退出吗?","退出",
                    JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);

            if (j == JOptionPane.YES_OPTION){
                System.exit(0);
            }
        }
    }

}//主框架类
