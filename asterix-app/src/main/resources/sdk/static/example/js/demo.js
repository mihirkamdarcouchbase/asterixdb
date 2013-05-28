$(document).ready(function() {

    // 0A - Exact-Match Lookup
    $('#run0a').click(function () {
        $('#result0a').html('');
        var expression0a = new FLWOGRExpression({
            "dataverse" : "TinySocial",
            "success"   : function(res) {
                            alert(JSON.stringify(res));
                            $('#result0a').html(res["results"]);
                          } 
            })
            .bind( new ForClause("user", null, new AsterixExpression().set(["dataset FacebookUsers"])) )
            .bind( new WhereClause(new BooleanExpression("$user.id = 8")) )
            .bind({ "return" : new AsterixExpression().set(["$user"]) });
        alert(expression0a.val());
        expression0a.run();
    });

    // 8 - Simple Aggregation
    $('#run8').click(function () {

        // Option 1: Simple, Object Syntax     
        $('#result8').html('');   
        var expression8 = new FunctionExpression({
            "function"      : "count",
            "expression"    : new ForClause(
                                "fbu", null, new AsterixExpression().set(["dataset FacebookUsers"])
                              ).bind(
                                {"return" : new AsterixExpression().set(["$fbu"])}
                              ),
            "dataverse"     : "TinySocial",
            "success"       : function(res) {
                                $('#result8').html(res["results"]);
                              }
        });
        expression8.run();
    });
    
});
