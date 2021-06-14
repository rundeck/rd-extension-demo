package org.rundeck.cli.demo.ext;

import lombok.Setter;
import org.rundeck.client.tool.extension.RdCommandExtension;
import org.rundeck.client.tool.extension.RdTool;
import org.rundeck.toolbelt.SubCommand;
import org.rundeck.toolbelt.ToolBelt;
import picocli.CommandLine;

import java.util.List;
import java.util.concurrent.Callable;

// using picocli @Command to define picocli interface
@CommandLine.Command(
        name = "pico",
        mixinStandardHelpOptions = true,
        version = "0.1-SNAPSHOT",
        description = "@|bold,underline Demo|@ use of @|fg(red) picocli|@.",
        subcommands = {
                Pico.Wave.class
        }
)
@SubCommand(path = {"ext"})
public class Pico
        implements RdCommandExtension, ToolBelt.CommandInvoker, Callable<Integer>
{
    @Setter RdTool rdTool;

    @CommandLine.Option(names = {"-n", "--name"})
    String name;

    @Override
    public Integer call() throws Exception {
        if (name != null) {
            System.out.println("Name is " + name);
        } else {
            System.out.println("What is your name? use --name <yourname>.  Or use wave to just wave at things");
        }
        return 0;
    }

    /**
     * Handle the argument list by invoking Picocli
     *
     * @param args
     */
    @Override
    public boolean run(final String[] args) {
        return new CommandLine(this).execute(args) == 0;
    }

    @Override
    public String getDescription() {
        return new CommandLine(this).getHelp().description();
    }

    @Override
    public void getHelp() {
        new CommandLine(this).usage(new CommandLine(this).getOut());
    }

    @CommandLine.Command(name = "wave", description = "wazzup")
    static class Wave
            implements Callable<Integer>

    {
        @CommandLine.Option(names = {"--thingies", "-t"}, required = true)
        List<String> thingies;

        @Override
        public Integer call() throws Exception {
            System.out.println("*waves at*");
            if (null != thingies) {
                thingies.forEach(a -> System.out.printf("* %s%n", a));
            }
            return 0;
        }
    }
}
