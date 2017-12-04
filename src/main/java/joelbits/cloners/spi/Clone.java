package joelbits.cloners.spi;

import java.util.List;

public interface Clone {
    void clone(String sourcePath, String destinationPath, List<String> repositories);
}
