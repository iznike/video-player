import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javafx.scene.text.Font;

public class Icon {
    
    public static Font fontAwesome;

    public static final String PLAY = "\uf04b";

    public static final String PAUSE = "\uf04c";

    public static final String EXPAND = "\uf065";

    public static final String COMPRESS = "\uf066";

    public static final String VOLUME_DOWN = "\uf027";

    public static final String VOLUME_MUTE = "\uf6a9";

    public static final String VOLUME_UP = "\uf028";

    public static final String ARROW_LEFT = "\uf060";

    public static final String FORWARD = "\uf04e";

    public static final String BACKWARD = "\uf04a";

    public static final String EDIT = "\uf044";

    public static final String CHEVRON_LEFT = "\uf053";

    public static final String CHEVRON_RIGHT = "\uf054";

    public static final String FILE_IMAGE = "\uf1c5";

    public static final String FILE_VIDEO = "\uf1c8";

    
    public static final void loadIconFont() {
        // Font font = null;
        try (InputStream fileInputStream = Icon.class.getClassLoader().getResourceAsStream("Font Awesome 5 Free-Solid-900.otf")) {
            fontAwesome = Font.loadFont(fileInputStream, 12);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static final Font getIconFont(double size) {
        return Font.font(fontAwesome.getFamily(), size);
    }

    

}
