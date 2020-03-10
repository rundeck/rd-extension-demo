package org.rundeck.cli.demo.ext;


import com.lexicalscope.jewel.cli.Option;
import com.lexicalscope.jewel.cli.Unparsed;
import org.rundeck.client.api.RundeckApi;
import org.rundeck.client.api.model.SystemInfo;
import org.rundeck.client.tool.InputError;
import org.rundeck.client.tool.extension.RdCommandExtension;
import org.rundeck.client.tool.extension.RdTool;
import org.rundeck.client.util.DataOutput;
import org.rundeck.client.util.ServiceClient;
import org.rundeck.toolbelt.Command;
import org.rundeck.toolbelt.CommandOutput;
import org.rundeck.toolbelt.SubCommand;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.*;

@SubCommand(path = {"ext", "demo"}, descriptions = {"a test subcommand", "subcommand demo"})
public class MyCommand
        implements RdCommandExtension
{
    private RdTool rdTool;


    @Override
    public void setRdTool(RdTool rdTool) {
        this.rdTool = rdTool;
    }

    @Command(description = "example command")
    public void example(CommandOutput output) {
        output.output("This is an example command");
    }

    /**
     * Uses JewelCLI to define input options
     */
    static interface MyInput {
        @Option(longName = "name", shortName = "n", description = "What is your name?")
        String getName();
    }

    @Command(description = "example for parsing input")
    public void inputs(CommandOutput output, MyInput input) {
        output.output(String.format("Hello, %s, nice to meet you.", input.getName()));
    }

    @Command(description = "displays the rundeck server API version")
    public void apiversion(CommandOutput output) throws InputError, IOException {
        Call<SystemInfo> systemInfoCall = rdTool.getClient().getService().systemInfo();
        Response<SystemInfo> execute = systemInfoCall.execute();
        output.info(String.format("Response: %d %s", execute.code(), execute.message()));
        output.output(String.format("API Version: %s", execute.headers().get("X-Rundeck-API-Version")));
    }

    @Command(description = "Makes API call using Rundeck API Client interface, requires authentication")
    public void apicall(CommandOutput output) throws InputError, IOException {
        SystemInfo systemInfo = rdTool.apiCall(RundeckApi::systemInfo);
        output.info("System info:");
        output.output(systemInfo);
    }

    @Command(description = "demonstrates basic data output for yaml/json scripting")
    public void scripting(CommandOutput output, ScriptingInput input) {
        ArrayList<String> names = new ArrayList<>();
        if (input.isArgs()) {
            names.addAll(input.getArgs());
        } else {
            names.add("Groucho");
            names.add("Chico");
            names.add("Harpo");
            names.add("Zeppo");
        }
        output.info("Emitting a list of input arguments. Use RD_FORMAT=yaml or RD_FORMAT=json to see the results");
        output.output(names);
    }

    /**
     * Uses JewelCLI to define input options
     */
    static interface ScriptingInput {
        @Unparsed

        List<String> getArgs();

        boolean isArgs();
    }

    /**
     * Demonstrates using {@link DataOutput} to emit structured data
     */
    static class MyCheeses
            implements DataOutput
    {
        List<String> cheeses;

        public MyCheeses(final List<String> cheeses) {
            this.cheeses = cheeses;
        }

        @Override
        public Map<?, ?> asMap() {
            HashMap<String, List<String>> map = new HashMap<>();
            map.put("cheeses", cheeses);
            return map;
        }
    }

    @Command(description = "demonstrates typed data output for yaml/json scripting")
    public void scripting2(CommandOutput output, ScriptingInput input) {
        ArrayList<String> cheeseInput = new ArrayList<>();
        if (input.isArgs()) {
            cheeseInput.addAll(input.getArgs());
        } else {
            cheeseInput.add("Gouda");
            cheeseInput.add("Cheddar");
            cheeseInput.add("Bleu");
            cheeseInput.add("Bleu");
        }
        output.info("Emitting a cheese map. Use RD_FORMAT=yaml or RD_FORMAT=json to see the results");
        output.output(new MyCheeses(cheeseInput));
    }


    @Command(description = "Makes API call using custom API interface")
    public void apicustom(CommandOutput output) throws InputError, IOException {
        ServiceClient<MyApi> client = rdTool.getRdApp().getClient(MyApi.class);
        RolesResult s = client.apiCall(MyApi::userRoles);

        output.info("User Roles:");
        s.getRoles().forEach(role -> output.output(String.format("* %s", role)));
    }

    /**
     * Demonstrates using Retrofit to define the API
     */
    static interface MyApi {

        @Headers("Accept: application/json")
        @POST("user/roles")
        Call<RolesResult> userRoles();
    }

    public static class RolesResult {
        private List<String> roles;

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
}
