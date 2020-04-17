<#import "/spring.ftl" as spring />
<#import "lib/template.ftl" as template>
<@template.header "Connector: ${connector.name}">
    <style type="text/css">
        #action-bar {
            margin-top: 17px;
        }

        th {
            word-break: break-all;
        }
    </style>
</@template.header>

<#setting number_format="0">

<h2>Connector: ${connector.name}</h2>

<br/>
<div class="container-fluid pl-0">
    <div class="row">
        <div id="connector-overview" class="col-md-6">
            <h3>Overview</h3>

            <table class="table table-bordered">
                <tbody>
                <tr>
                    <td>Connector status</td>
                    <td>
                        <#if connectorStatus.status == "FAILED">
                            <span class="text-danger"><i class="fa fa-warning"></i> ${connectorStatus.status}</span>
                        <#else>
                            <span class="text-success"><i class="fa fa-bolt"></i> ${connectorStatus.status}</span>
                        </#if>
                    </td>
                </tr>
                <tr>
                    <td># of tasks</td>
                    <td>${connectorStatus.taskList?size}</td>
                </tr>
                </tbody>
            </table>
        </div>

        <div id="connector-tasks" class="col-md-6">
            <h3>Tasks</h3>

            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Task ID</th>
                        <th>State</th>
                        <th>Worker ID</th>
                    </tr>
                </thead>
                <tbody>
                <#list connectorStatus.taskList as t>
                    <tr>
                        <td>${t.id}</td>
                        <#if t.state == "FAILED">
                            <td class="danger text-danger"><i class="fa fa-warning"></i> ${t.state}</td>
                        <#else>
                            <td class="success text-success"><i class="fa fa-bolt"></i> ${t.state}</td>
                        </#if>
                        <td>${t.workerId}</td>
                        <td class="text-center"><a class="btn btn-outline-light" href="<@spring.url '/connect/restartTask/${connector.name}/${t.id}'/>"><i class="fa fa-cogs"></i>&nbsp;&nbsp;Restart Task</a></td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row">
        <h3 class="col-md-5">Configuration</h3>
        <div class="button-containers col-md-7">
            <button id="updateConfigButton" class="offset-md-7 col-md-5 btn btn-outline-light">Update Configuration</button>
        </div>
    </div>

    <div id="jsonEditor" class="row mt-md-2" style="height:500px;"></div>
</div>

<script>
    // create the editor
    const container = document.getElementById("jsonEditor");
    const options = {mode: 'code'};
    let initialJson = ${connector.prettyPrintConfig(connector.configJson)};
    const editor = new JSONEditor(container, options, initialJson);

    // update config with json
    $(document).ready(function(){
        $("#updateConfigButton").click(function (){
            if (confirm("Are you sure you want to update the configuration?")) {
                let json = JSON.stringify(editor.get());
                $.ajax({
                        type: "POST",
                        url: "<@spring.url '/connect/upsertConfiguration/${connector.name}' />",
                        data: btoa(json),
                        cache: false,
                        timeout: 10000,
                        success: function () {
                            console.log('posted updated config');
                            location.replace("<@spring.url '/' />");
                        },
                        error: function (e) {
                            console.log("ERROR: ", e);
                        }
                    }
                );
            }
        });
    });
</script>
<@template.footer/>