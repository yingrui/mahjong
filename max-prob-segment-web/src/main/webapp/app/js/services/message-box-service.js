corpusEditorServices.
    factory('MessageBox', function () {

        var message = {
            title: "",
            content: "",
            type: "warning",
            id: 0
        };

        return {
            showWarning: function(title, content) {
                message.title = title;
                message.content = content;
                message.type = "warning";
                message.id = message.id + 1;
            },
            getMessage: function() {
                return message;
            }
        };

    });
