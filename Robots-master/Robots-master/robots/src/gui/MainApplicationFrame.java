package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private INI options = new INI();
    private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() throws IOException {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);

        ArrayList<Object_Window> obj = options.LoadINI();

        setJMenuBar(generateMenuBar());

        addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {
                if(obj!=null)
                for (int i=0; i<obj.size();i++) {
                    switch (obj.get(i).Title)
                    {
                        case "Информер игры" :
                            break;
                        case "Лог" : createLogWindow(obj.get(i));
                        break;
                        case "Игровое поле": createGameWindow(obj.get(i));
                        break;
                    }
                }
                else
                {
                    Object_Window gamewin = new Object_Window ("Игровое поле",230,10,400,400);
                    createGameWindow(gamewin);
                    createLogWindow(new Object_Window ("Лог",10,10,700,200));

                }
            }

            public void windowClosing(WindowEvent e)   {
                Object[] option = { "Да", "Нет!" };
                int n = JOptionPane
                        .showOptionDialog(e.getWindow(), "Закрыть окно?",
                                "Подтверждение", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, option,
                                option[0]);
                if (n == 0) {
                    SaveINI();
                    e.getWindow().setVisible(false);
                    System.exit(0);
                }
            }

        });
        getComponent(0);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void SaveINI(){
        options = new INI();
        int count = desktopPane.getComponentCount();
        if(count==0) options.addRecord(null,0,0,0,0,null);
        else {
            Stack<Object_Window> stack = new Stack<Object_Window>();
            for (int i = 0; i < count; i++) {
                Component comp = desktopPane.getComponent(i);
                String title = comp.getName();
                if (title.equals("Информер игры")) continue;
                Object_Window informer = null;


                int x = comp.getX();
                int y = comp.getY();
                int w = comp.getWidth();
                int h = comp.getHeight();


                if(comp instanceof GameWindow) {
                    GameWindow gw = (GameWindow) comp;
                    RobotInfo robotInf = (RobotInfo)gw.gameINFO;
                    if(!robotInf.isClosed()) informer = new Object_Window(robotInf.getTitle(),robotInf.getX(),robotInf.getY(),robotInf.getHeight(),robotInf.getWidth());
                    else informer = new Object_Window("Информер игры",x+h,y,w,250);

                }

                switch (title)
                {
                    case "Лог" : options.addRecord(title, x, y, h, w,null);
                        break;
                    case "Игровое поле":
                            options.addRecord(title, x, y, h, w,informer);
                        break;
                }

            }
        }
        options.Save();
    }

    protected void createLogWindow(Object_Window log)
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(log.X,log.Y);
        logWindow.setSize(log.W, log.H);
        logWindow.setName("Лог");
        //setMinimumSize(logWindow.getSize());
        Logger.debug("Протокол работает");
        addWindow(logWindow);
    }


    protected void createGameWindow(Object_Window game)
    {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(game.W, game.H);
        gameWindow.setLocation(game.X,game.Y);
        gameWindow.setName("Игровое поле");
        addWindow(gameWindow);
        Object_Window gameInfo = game.podWindow;
        if(gameInfo!=null)
            addWindow(gameWindow.createGameInfo(gameInfo));
        else  addWindow(gameWindow.createGameInfo(new Object_Window("Информер игры",game.X+game.W,game.Y,game.H,250)));

    }


    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Set up the lone menu.
        JMenu menu = new JMenu("Document");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new");
//        menuItem.addActionListener(this);
        menu.add(menuItem);

       //Set up the second menu item.
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
     //   menuItem.addActionListener((ActionListener) this);
       menu.add(menuItem);

        return menuBar;
    }
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        
        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu Menu = new JMenu("Окно игры");
        Menu.setMnemonic(KeyEvent.VK_T);
        {
            JMenuItem addLogMessageItem = new JMenuItem("Добавить окно игры", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Игровое окно добавлено");
                Object_Window gamewin = new Object_Window ("Игровое поле",230,10,400,400);
                gamewin.addPodWindow("Информер игры",230+400,10,400,250);
                createGameWindow(gamewin);

            });
            Menu.add(addLogMessageItem);

            JMenuItem addLogMessageItem1 = new JMenuItem("Добавить окно лога", KeyEvent.VK_S);
            addLogMessageItem1.addActionListener((event) -> {

                createLogWindow(new Object_Window("Лог",10,10,800,200));
            });
            Menu.add(addLogMessageItem1);

        }

        JMenuItem exit = new JMenuItem("Выход", KeyEvent.VK_S);
        exit.addActionListener((event) -> {
                SaveINI();
                System.exit(0);
                });

        menuBar.add(lookAndFeelMenu);
        menuBar.add(Menu);
        menuBar.add(exit);
        return menuBar;
    }
    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
