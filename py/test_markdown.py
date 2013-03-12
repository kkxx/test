# easy_install Markdown
# easy_install Pygments
# python -m markdown -x tables -x codehilite note.md > note.html

import codecs
import markdown

input_file = codecs.open("note.md", mode="r", encoding="utf-8")
text = input_file.read()
html = markdown.markdown(text, ['codehilite(force_linenos=True)', 'tables'])

output_file = codecs.open("note.html", "w", 
                          encoding="utf-8", 
                          errors="xmlcharrefreplace"
)
output_file.write(html)

#html = markdown.markdown("print 'test'", ['codehilite'])
#print html