package joelbits.plugins.cloners;

import com.google.auto.service.AutoService;
import joelbits.plugins.Settings;
import joelbits.plugins.cloners.spi.Clone;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoService(Clone.class)
public class GitHubCloner implements Clone {
    private static final Logger log = LoggerFactory.getLogger(GitHubCloner.class);
    private static final String GITHUB = "https://github.com/";

    /**
     * Clones git repositories from GitHub to local directory.
     *
     * @param repositories      the full name of git repositories to clone, e.g., JCTools/JCTools
     */
    @Override
    public void clone(List<String> repositories) {
        ExecutorService executorService = Executors.newFixedThreadPool(new Settings().numberOfThreads());

        for (String repository : repositories) {
            String remoteRepositoryPath = GITHUB + repository + Constants.DOT_GIT;
            String localRepositoryPath = System.getProperty("user.dir") + File.separator + repository;

            executorService.execute(new Cloner(remoteRepositoryPath, localRepositoryPath));
        }
        executorService.shutdown();
        log.info("Cloning completed");
    }

    class Cloner implements Runnable {
        private final String sourcePath;
        private final String destinationPath;

        Cloner(String sourcePath, String destinationPath) {
            this.sourcePath = sourcePath;
            this.destinationPath = destinationPath;
        }

        @Override
        public void run() {
            try (Git git = Git.cloneRepository()
                    .setURI(sourcePath)
                    .setDirectory(Paths.get(destinationPath).toFile())
                    .call()) {

                log.info(sourcePath + " cloned");
            } catch (GitAPIException e) {
                log.error(e.toString(), e);
            }
        }
    }

    @Override
    public String toString() {
        return "GitHubCloner";
    }
}
