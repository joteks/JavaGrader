mkdir $HOME/asnlib >& temp.txt

cp -a $ASNLIB/*.java $HOME/asnlib >& temp.txt

cp $ASNLIB/Project.class $HOME >& temp.txt

cp $LIB/Parser.class $HOME >& temp.txt

cp $LIB/TestCase.class $HOME >& temp.txt

java -classpath "$LIB/javaparser-core-3.2.7.jar:." Project

cat $HOME/gradefile.txt > $vocareumGradeFile

rm -r $HOME/asnlib >& temp.txt

rm -r $HOME/temp* >& temp.txt

rm *.class >& temp.txt

rm *.txt >& temp.txt

echo " " > temp.txt

rm temp.txt
