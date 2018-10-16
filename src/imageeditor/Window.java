package imageeditor;

import java.awt.Container;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Window extends JFrame {

    //Atributes
    private Container container = getContentPane();
    private Viewer viewer;

    //Constructor
    public Window() {
        super("Editor de imagenes.");
        //Crear canvas
        this.viewer = new Viewer();
        //Ventana con tamaño de la imagen
        setSize((this.viewer.getImageWidth()) + 20, this.viewer.getImageHeight() + 40);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container.setLayout(new GridBagLayout());
        //Añadir imagenes a la ventana
        container.add(this.viewer);
        this.setVisible(true);
        this.viewer.melt(20, 1, this.viewer.getGraphics());
    }

    //Methods
}
