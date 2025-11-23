/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */

import java.net.*;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * The server to be used to dispatch packets between clients. Comes with a nice
 * GUI.
 * The server is needed to stored records, serve multi-users games (including
 * the ones against AI players) and single users games.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

// options : rankedboards path, server port
// start, pause, stop
// list aacounts, rankedboards, packets sent per second, solutions/scoress
// received per second
public class GameServer extends JFrame {

    private static final Logger logger = LogManager.getLogger(GameServer.class);

    private final static ResourceBundle GameResourceBundle = ResourceBundle.getBundle("org.game.Game.Game");

    private final static String LOCALHOST = "127.0.0.1";

    private final static int NOTREADY = -1;
    private final static int STOPPED = 0;
    private final static int STARTED = 1;
    private final static int PAUSED = 2;

    private static final String ServerCommandRegister = "REGISTER";
    private static final String ServerCommandLogin = "LOGIN";
    private static final String ServerCommandDeleteAccount = "DELETEACCOUNT";
    private static final String ServerCommandLogout = "LOGOUT";
    private static final String ServerCommandGetRanked = "GETRANKED";

    private int state;

    private InetAddress serverIP;
    private int serverPort;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private BufferedReader bufferedReader;

    // UI Components
    private JFrame frame;
    private Container contentPane;
    private JPopupMenu popupMenu;
    private ButtonGroup popupMenuGroup;
    private AboutDialog aboutBox;
    private JTextField statusField;

    // Application tracking
    private static int numSSs = 0;
    private static List<GameServer> GameServerApplications = new ArrayList<>();

    // Configuration
    private static ResourceBundle gameResourceBundle;
    private String serverAddress;

    // Look and Feel data
    private static LookAndFeelData[] lookAndFeelData = {
            new LookAndFeelData("Metal", "javax.swing.plaf.metal.MetalLookAndFeel", "Metal Look and Feel"),
            new LookAndFeelData("Nimbus", "javax.swing.plaf.nimbus.NimbusLookAndFeel", "Nimbus Look and Feel"),
            new LookAndFeelData("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
                    "Windows Look and Feel"),
            new LookAndFeelData("System", UIManager.getSystemLookAndFeelClassName(), "System Look and Feel")
    };

    public GameServer(@NotNull InetAddress serverIP, int serverPort) {
        state = NOTREADY;
        if ((serverIP != null)) {
            this.serverIP = serverIP;
            this.serverPort = serverPort;
        } else
            throw new IllegalArgumentException("serverIP can't be null.");
    }

    public GameServer(int serverPort) throws UnknownHostException {
        new GameServer(InetAddress.getByName(getIPAddress()), serverPort);
    }

    public GameServer() throws UnknownHostException {
        new GameServer(new Integer(gameResourceBundle.getString("ServerPort")).intValue());
    }

    public InetAddress getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void start() throws IOException {
        if (state == STOPPED || state == NOTREADY) {
            logger.info("Server starting.");
            serverSocket = new ServerSocket(serverPort);
            socket = serverSocket.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            state = STARTED;
            logger.info("Server started.");
        } else
            throw new IllegalStateException("State must be NOTREADY or STOPPED.");
    }

    public void play() throws IOException {
        if (state == STARTED) {
            // run in background
            // create new handle for every known client
            // send packet to client when asked
            String str = "", str2 = "";
            while (!str.equals("stop")) {
                str = dataInputStream.readUTF();
                System.out.println("client says: " + str);
                str2 = bufferedReader.readLine();
                dataOutputStream.writeUTF(str2);
                dataOutputStream.flush();
            }
        } else
            throw new IllegalStateException("State must be STARTED.");
    }

    public void pause() {
        if (state == STARTED) {
            state = PAUSED;
            logger.info("Server paused.");
        } else
            throw new IllegalStateException("State must be STARTED.");
    }

    public void stop() throws IOException {
        if (state == STARTED || state == PAUSED) {
            dataInputStream.close();
            socket.close();
            serverSocket.close();
            state = STOPPED;
            logger.info("Server stopped.");
        } else
            throw new IllegalStateException("State must be STARTED or PAUSED.");
    }

    public GameUser registerAccount(@NotNull String login, @NotNull String passwordHash) {
        throw new RuntimeException("Not yet implemented.");
    }

    public void changePassword(@NotNull String login, @NotNull String oldPasswordHash,
            @NotNull String newPasswordHash) {
        throw new RuntimeException("Not yet implemented.");
    }

    public void deleteAccount(@NotNull String login, @NotNull String passwordHash) {
        throw new RuntimeException("Not yet implemented.");
    }

    public void sendPacket(@NotNull GamePacket GamePacket) {
        throw new RuntimeException("Not yet implemented.");
    }

    public GamePacket receivePacket(@NotNull GameUser GameUser) {
        throw new RuntimeException("Not yet implemented.");
    }

    public void main(String[] args) {
        JFrame frame = new JFrame("JFrame Example");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("JFrame By Example");
        JButton button = new JButton();
        button.setText("Button");
        panel.add(label);
        panel.add(button);
        frame.add(panel);
        frame.setSize(200, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            System.out.println("g is null");
            return;
        }
        for (int i = 0; i < 100; i++) {
            renderSplashFrame(g, i);
            splash.update();
            try {
                Thread.sleep(90);
            } catch (InterruptedException e) {
            }
        }
        splash.close();

        // addRankedGameBoards
        // showRankdedBoards

        // load users database

        GameServer GameServer;
        try {
            GameServer = new GameServer(InetAddress.getByName(serverAddress), serverPort);
        } catch (UnknownHostException ex) {
            throw new RuntimeException("Invalid server address", ex);
        }
        GameServer.pack();
        GameServer.setVisible(true);
        setVisible(true);
        toFront();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] components = { "boards", "users", "server" };
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120, 140, 200, 40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Loading " + components[(frame / 5) % 3] + "...", 120, 150);
    }

    private static final String getIPAddress() {
        String ip;
        ip = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (networkInterface.isLoopback() || !networkInterface.isUp())
                    continue;
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    ip = address.getHostAddress();
                    // System.out.println(networkInterface.getDisplayName() + " " + ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }

    public JMenuBar createMenus() {
        JMenuItem mi;
        // ***** create the menuBar ****
        JMenuBar menuBar = new JMenuBar();
        menuBar.getAccessibleContext().setAccessibleName(
                getString("Server.MenuBar.accessible_description"));

        // ***** create File menu
        JMenu fileMenu = menuBar.add(new JMenu(getString("Server.FileMenu.file_label")));
        fileMenu.setMnemonic(getMnemonic("Server.FileMenu.file_mnemonic"));
        fileMenu.getAccessibleContext().setAccessibleDescription(getString("Server.FileMenu.accessible_description"));

        createMenuItem(fileMenu, "Server.FileMenu.about_label", "Server.FileMenu.about_mnemonic",
                "Server.FileMenu.about_accessible_description", new GameServer.AboutAction(this));

        fileMenu.addSeparator();

        createMenuItem(fileMenu, "Server.FileMenu.open_label", "Server.FileMenu.open_mnemonic",
                "Server.FileMenu.open_accessible_description", null);

        createMenuItem(fileMenu, "Server.FileMenu.save_label", "Server.FileMenu.save_mnemonic",
                "Server.FileMenu.save_accessible_description", null);

        createMenuItem(fileMenu, "Server.FileMenu.save_as_label", "Server.FileMenu.save_as_mnemonic",
                "Server.FileMenu.save_as_accessible_description", null);

        fileMenu.addSeparator();

        createMenuItem(fileMenu, "Server.FileMenu.exit_label", "Server.FileMenu.exit_mnemonic",
                "Server.FileMenu.exit_accessible_description", new GameServer.ExitAction(this));

        return (JMenuBar) super.getJMenuBar();
    }

    /**
     * Create a checkbox menu menu item
     */
    private JMenuItem createCheckBoxMenuItem(JMenu menu, String label,
            String mnemonic,
            String accessibleDescription,
            Action action) {
        JCheckBoxMenuItem mi = (JCheckBoxMenuItem) menu.add(
                new JCheckBoxMenuItem(getString(label)));
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(
                accessibleDescription));
        mi.addActionListener(action);
        return mi;
    }

    /**
     * Creates a generic menu item
     */
    public JMenuItem createMenuItem(JMenu menu, String label, String mnemonic,
            String accessibleDescription, Action action) {
        JMenuItem mi = menu.add(new JMenuItem(getString(label)));
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(accessibleDescription));
        mi.addActionListener(action);
        if (action == null) {
            mi.setEnabled(false);
        }
        return mi;
    }

    public JPopupMenu createPopupMenu() {
        JPopupMenu popup = new JPopupMenu("JPopupMenu demo");

        for (LookAndFeelData lafData : lookAndFeelData) {
            createPopupMenuItem(popup, lafData);
        }

        // register key binding to activate popup menu
        InputMap map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.SHIFT_DOWN_MASK),
                "postMenuAction");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTEXT_MENU, 0), "postMenuAction");
        getActionMap().put("postMenuAction", new GameServer.ActivatePopupMenuAction(this, popup));

        return popup;
    }

    /**
     * Creates a JMenuItem for the Look and Feel popup menu
     */
    public JMenuItem createPopupMenuItem(JPopupMenu menu, LookAndFeelData lafData) {
        JMenuItem mi = menu.add(new JMenuItem(lafData.label));
        popupMenuGroup.add(mi);
        mi.setMnemonic(lafData.mnemonic);
        mi.getAccessibleContext().setAccessibleDescription(lafData.accDescription);
        mi.addActionListener(new ChangeLookAndFeelAction((JFrame) this, lafData));
        return mi;
    }

    public void showGameServerApplication() {
        // put GameServerApplication in a frame and show it
        JFrame f = getFrame();
        f.setTitle(getString("Frame.title"));
        f.getContentPane().add(this, BorderLayout.CENTER);
        f.pack();

        Rectangle screenRect = f.getGraphicsConfiguration().getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                f.getGraphicsConfiguration());

        // Make sure we don't place the demo off the screen.
        int centerWidth = screenRect.width < f.getSize().width ? screenRect.x
                : screenRect.x + screenRect.width / 2 - f.getSize().width / 2;
        int centerHeight = screenRect.height < f.getSize().height ? screenRect.y
                : screenRect.y + screenRect.height / 2 - f.getSize().height / 2;

        centerHeight = Math.max(centerHeight, screenInsets.top);

        f.setLocation(centerWidth, centerHeight);
        f.setVisible(true);
        numSSs++;
        GameServerApplications.add(this);
    }

    /**
     * Returns the frame instance
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Returns the menuBar
     */
    public JMenuBar getJMenuBar() {
        return (JMenuBar) super.getJMenuBar();
    }

    /**
     * Returns the content pane whether we're in an applet
     * or application
     */
    public Container getContentPane() {
        if (contentPane == null) {
            if (getFrame() != null) {
                contentPane = getFrame().getContentPane();
            }
        }
        return contentPane;
    }

    /**
     * Create a frame for GameServerApplication to reside in if brought up
     * as an application.
     */
    public static JFrame createFrame(GraphicsConfiguration gc) {
        JFrame frame = new JFrame(gc);
        if (numSSs == 0) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            WindowListener l = new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    numSSs--;
                    GameServerApplications.remove(this);
                }
            };
            frame.addWindowListener(l);
        }
        return frame;
    }

    /**
     * Set the status
     */
    public void setStatus(String s) {
        // do the following on the gui thread
        SwingUtilities.invokeLater(new GameServer.GameServerApplicationRunnable(this, s) {
            public void run() {
                GameServerApplication.statusField.setText((String) obj);
            }
        });
    }

    /**
     * This method returns a string from the demo's resource bundle.
     */
    public static String getString(String key) {
        String value = null;
        try {
            value = TextAndMnemonicUtils.getTextAndMnemonicString(key);
        } catch (MissingResourceException e) {
            System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
        }
        if (value == null) {
            value = "Could not find resource: " + key + "  ";
        }
        return value;
    }

    /**
     * Returns a mnemonic from the resource bundle. Typically used as
     * keyboard shortcuts in menu items.
     * Removed - using static version from TextAndMnemonicUtils
     */
    /*
     * public char getMnemonic(String key) {
     * return (getString(key)).charAt(0);
     * }
     */

    /**
     * Creates an icon from an image contained in the "images" directory.
     */
    public ImageIcon createImageIcon(String filename, String description) {
        String path = "/resources/images/" + filename;
        return new ImageIcon(getClass().getResource(path));
    }

    private void updateThisGameServerApplication() {
        JFrame frame = getFrame();
        if (frame == null) {
            SwingUtilities.updateComponentTreeUI(this);
        } else {
            SwingUtilities.updateComponentTreeUI(frame);
        }

        SwingUtilities.updateComponentTreeUI(popupMenu);
        if (aboutBox != null) {
            SwingUtilities.updateComponentTreeUI(aboutBox);
        }
    }

    // *******************************************************
    // ************** ToggleButtonToolbar *****************
    // *******************************************************
    static Insets zeroInsets = new Insets(1, 1, 1, 1);

    protected static class ToggleButtonToolBar extends JToolBar {
        public ToggleButtonToolBar() {
            super();
            setFloatable(true);
            setRollover(true);
            // https://stackoverflow.com/questions/19070324/how-to-remove-replace-the-close-button-from-a-floating-jtoolbar
        }

        JToggleButton addToggleButton(Action a) {
            JToggleButton tb = new JToggleButton(
                    (String) a.getValue(Action.NAME),
                    (Icon) a.getValue(Action.SMALL_ICON));
            tb.setMargin(zeroInsets);
            tb.setText(null);
            tb.setEnabled(a.isEnabled());
            tb.setToolTipText((String) a.getValue(Action.SHORT_DESCRIPTION));
            tb.setAction(a);
            add(tb);
            return tb;
        }
    }

    // *******************************************************
    // ********* ToolBar Panel / Docking Listener ***********
    // *******************************************************
    static class ToolBarPanel extends JPanel implements ContainerListener {

        public boolean contains(int x, int y) {
            Component c = getParent();
            if (c != null) {
                Rectangle r = c.getBounds();
                return (x >= 0) && (x < r.width) && (y >= 0) && (y < r.height);
            } else {
                return super.contains(x, y);
            }
        }

        public void componentAdded(ContainerEvent e) {
            Container c = e.getContainer().getParent();
            if (c != null) {
                c.getParent().validate();
                c.getParent().repaint();
            }
        }

        public void componentRemoved(ContainerEvent e) {
            Container c = e.getContainer().getParent();
            if (c != null) {
                c.getParent().validate();
                c.getParent().repaint();
            }
        }
    }

    // *******************************************************
    // ****************** Runnables ***********************
    // *******************************************************

    /**
     * Generic GameServerApplication runnable. This is intended to run on the
     * AWT gui event thread so as not to muck things up by doing
     * gui work off the gui thread. Accepts a GameServerApplication and an Object
     * as arguments, which gives subtypes of this class the two
     * "must haves" needed in most runnables for this demo.
     */
    static class GameServerApplicationRunnable implements Runnable {
        protected GameServer GameServerApplication;
        protected Object obj;

        public GameServerApplicationRunnable(GameServer GameServerApplication, Object obj) {
            this.GameServerApplication = GameServerApplication;
            this.obj = obj;
        }

        public void run() {
        }
    }

    // *******************************************************
    // ******************** Actions ***********************
    // *******************************************************

    class ActivatePopupMenuAction extends AbstractAction {
        GameServer GameServerApplication;
        JPopupMenu popup;

        protected ActivatePopupMenuAction(GameServer GameServerApplication, JPopupMenu popup) {
            super("ActivatePopupMenu");
            this.GameServerApplication = GameServerApplication;
            this.popup = popup;
        }

        public void actionPerformed(ActionEvent e) {
            Dimension invokerSize = getSize();
            Dimension popupSize = popup.getPreferredSize();
            popup.show(GameServerApplication, (invokerSize.width - popupSize.width) / 2,
                    (invokerSize.height - popupSize.height) / 2);
        }
    }

    static class ExitAction extends AbstractAction {
        GameServer GameServerApplication;

        protected ExitAction(GameServer GameServerApplication) {
            super("ExitAction");
            this.GameServerApplication = GameServerApplication;
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class AboutAction extends AbstractAction {
        GameServer GameServerApplication;

        protected AboutAction(GameServer GameServerApplication) {
            super("AboutAction");
            this.GameServerApplication = GameServerApplication;
        }

        public void actionPerformed(ActionEvent e) {
            if (aboutBox == null) {
                aboutBox = new AboutDialog(GameServerApplication);
            }
            aboutBox.pack();
            aboutBox.setLocationRelativeTo(getFrame());
            aboutBox.setVisible(true);
        }
    }

    static class ModuleLoadThread extends Thread {
        GameServer GameServerApplication;

        ModuleLoadThread(GameServer GameServerApplication) {
            this.GameServerApplication = GameServerApplication;
        }

        public void run() {
            SwingUtilities.invokeLater(() -> GameServer.loadModules());
        }
    }

    /**
     * Gets the input map for key bindings.
     */
    public InputMap getInputMap(int condition) {
        return getRootPane().getInputMap(condition);
    }

    /**
     * Gets the action map for actions.
     */
    public ActionMap getActionMap() {
        return getRootPane().getActionMap();
    }

    /**
     * Loads game modules.
     * TODO: Implement module loading functionality
     */
    public static void loadModules() {
        // TODO: Implement module loading
    }

    /**
     * Gets mnemonic from resource key.
     * 
     * @param key the resource key
     * @return the mnemonic character code
     */
    public static int getMnemonic(String key) {
        return TextAndMnemonicUtils.getMnemonic(key);
    }
}
