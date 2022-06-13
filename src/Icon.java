import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.text.Font;

public class Icon {
    
    public static Font fontAwesome12;
    public static Font fontAwesome18;

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

    public static final void loadFonts() {
        fontAwesome12 = loadFontBySize(12);
        fontAwesome18 = loadFontBySize(18);
    }
    
    private static final Font loadFontBySize(double size) {
        Font font = null;
        try (FileInputStream fileInputStream = new FileInputStream("src\\Font Awesome 5 Free-Solid-900.otf")) {
            font = Font.loadFont(fileInputStream, size);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return font;
    }

}
