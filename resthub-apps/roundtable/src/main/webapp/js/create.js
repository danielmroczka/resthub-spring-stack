/**
 * Round Table creation component.
 */
var RoundTableCreateComponent = Class.extend({
    // constructor
    init: function(anchor) {
        this.anchor = anchor;

        var tpl = '<li>' +
            '<div><input type="checkbox" name="answers" value="<%= answer %>"></div>' +
            '<span><%= answer %></span>' +
            '</li>';


        var poll = {
            "id": null, "author": null, "topic": "New poll", "body": "",
            "answers": [
                {body: "Lorem ipsum dolor sit amet,"},
                {body: "consectetur adipisicing elit,"},
                {body: "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."}]
        };

        this.anchor.ejs('template/poll/create.ejs', poll);

        $('textarea.resizable:not(.processed)').TextAreaResizer();
        $('#create-rt form').validate({
            errorPlacement: function(error, element) {
                if (element.hasClass('resizable')) {
                    error.insertAfter(element.next());
                }
                else {
                    error.insertAfter(element);
                }
            }
        });
        $('#create-rt-answers').sortable({
            placeholder: 'answer-placeholder'
        });
        $('#create-rt-answers').disableSelection();

        $('#inp-del-answer').click(function() {
            $('input:checkbox[name=answers]:checked').parents('li').remove();
        });

        $('#inp-add-answer').click(function() {
            var answer = $('#inp-answer').val().trim();
            if (answer != '') {
                $("#create-rt-answers").ejs('', {answer:answer}, {text:tpl});
                $("#inp-answer").val("");
            }
        });
    },
    // methods
    toString: function() {
        return "";
    }
});