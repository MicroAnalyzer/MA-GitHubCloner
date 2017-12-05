package joelbits.cloners;

import com.google.auto.service.AutoService;
import joelbits.Settings;
import joelbits.cloners.spi.Clone;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoService(Clone.class)
public class GitCloner implements Clone {
    private static final Logger log = LoggerFactory.getLogger(GitCloner.class);

    /**
     * Clones git repositories from one location to another.
     *
     * @param sourcePath        the path of the git repositories to clone, e.g., https://github.com/
     * @param destinationPath   the path to where the cloned repositories should end up (locally)
     * @param repositories      the full name of git repositories to clone, e.g., JCTools/JCTools
     */
    @Override
    public void clone(String sourcePath, String destinationPath, List<String> repositories) {
        ExecutorService executorService = Executors.newFixedThreadPool(new Settings().numberOfThreads());

        for (String repository : repositories) {
            String remoteRepositoryPath = sourcePath + repository + Constants.DOT_GIT;
            String localRepositoryPath = destinationPath + repository;

            executorService.execute(new Cloner(remoteRepositoryPath, localRepositoryPath));
        }
        executorService.shutdown();
        log.info("Cloning completed");
    }

    class Cloner implements Runnable {
        private final String sourcePath;
        private final String destinationPath;

        public Cloner(String sourcePath, String destinationPath) {
            this.sourcePath = sourcePath;
            this.destinationPath = destinationPath;
        }

        @Override
        public void run() {
            try (Git git = Git.cloneRepository()
                    .setURI(sourcePath)
                    .setDirectory(Paths.get(destinationPath).toFile())
                    .call()) {
            } catch (GitAPIException e) {
                log.error(e.toString(), e);
            }
        }
    }

    @Override
    public String toString() {
        return "GitCloner";
    }
}
