#!/bin/bash

# normalize
echo "replace 'type variable[]' to 'type[] variable'"
find src -name "*.scala" -exec perl -p -i -e "s/\b([A-z0-9]+) ([a-z][A-z0-9<>,]+)\[\](\)| =|;)/\1\[\] \2\3/g" {} \;

# remove final type
echo "replace 'final type' to 'type'"
find src -name "*.scala" -exec perl -p -i -e "s/\bfinal //g" {} \;

# Primary Type
echo "replace ' int ' to ' Int '"
find src -name "*.scala" -exec perl -p -i -e "s/\b(int|Integer)\b/Int/g" {} \;
echo "replace ' boolean ' to ' Boolean '"
find src -name "*.scala" -exec perl -p -i -e "s/\bboolean\b/Boolean/g" {} \;
echo "replace ' double ' to ' Double '"
find src -name "*.scala" -exec perl -p -i -e "s/\bdouble\b/Double/g" {} \;
echo "replace ' float ' to ' Float '"
find src -name "*.scala" -exec perl -p -i -e "s/\bfloat\b/Float/g" {} \;
echo "replace ' long ' to ' Long '"
find src -name "*.scala" -exec perl -p -i -e "s/\blong\b/Long/g" {} \;
echo "replace ' byte ' to ' Byte '"
find src -name "*.scala" -exec perl -p -i -e "s/\bbyte\b/Byte/g" {} \;
echo "replace ' char ' to ' Char '"
find src -name "*.scala" -exec perl -p -i -e "s/\bchar\b/Char/g" {} \;

# Array Type
echo "replace 'type[]' to 'Array[type]'"
find src -name "*.scala" -exec perl -p -i -e "s/\b([A-z]+)\[\]/Array\[\1\]/g" {} \;
echo "replace 'new type[num]' to 'new Array[type](num)'"
find src -name "*.scala" -exec perl -p -i -e "s/\bnew ([A-z]+)\[([A-z0-9]+)\];/new Array\[\1\](\2);/g" {} \;
echo "replace 'array[i][j]' to 'array(i)(j)'"
find src -name "*.scala" -exec perl -p -i -e "s/\b([a-z][A-z0-9]*)\[([a-z][A-z0-9]*|[0-9]+)\]\[([a-z][A-z0-9]*|[0-9]+)\]/\1(\2)(\3)/g" {} \;
echo "replace 'array[index]' to 'array(index)'"
find src -name "*.scala" -exec perl -p -i -e "s/\b([a-z][A-z0-9]*)\[([a-z][A-z0-9]*|[0-9]+)\]/\1(\2)/g" {} \;

# override method
echo "replace '@Override\n' to 'override'"
find src -name "*.scala" -exec perl -p -i -e "s/\@Override\s*\n\s*/override /g" {} \;
find src -name "*.scala" -exec perl -p -i -e "s/override\s+/override /g" {} \;

# implements interface
echo "replace 'interface' to 'trait'"
find src -name "*.scala" -exec perl -p -i -e "s/^public interface/trait/g" {} \;
echo "replace 'implements' to 'extends'"
find src -name "*.scala" -exec perl -p -i -e "s/\bimplements\b/extends/g" {} \;
echo "replace 'method throws Exception;' to 'method;'"
find src -name "*.scala" -exec perl -p -i -e "s/ throws.*Exception;/;/g" {} \;

echo "replace 'public class' to 'class'"
find src -name "*.scala" -exec perl -p -i -e "s/^public (final )?class/class/g" {} \;

# define method
echo "replace 'public void method() ' to 'def method() '"
find src -name "*.scala" -exec perl -p -i -e "s/(public|protected) void ([A-z0-9]+)\((.*)\)( |;)/def \2(\3) /g" {} \;
echo "replace 'public type method() ' to 'def method() : type = '"
find src -name "*.scala" -exec perl -p -i -e "s/(public|protected) ([A-z0-9<>]+) ([A-z0-9]+)\((.*)\) /def \3(\4) : \2 = /g" {} \;
echo "replace 'public type method();' to 'def method() : type'"
find src -name "*.scala" -exec perl -p -i -e "s/public ([A-z0-9<>]+) ([A-z0-9]+)\((.*)\);/def \2(\3) : \1/g" {} \;

# define private method
echo "replace 'private void method() ' to 'private def method() '"
find src -name "*.scala" -exec perl -p -i -e "s/private void ([A-z0-9]+)\((.*)\) /private def \1(\2) /g" {} \;

echo "replace 'private type method() ' to 'private def method() : type = '"
find src -name "*.scala" -exec perl -p -i -e "s/private ([A-z0-9<>,]{4,}|Int) ([A-z0-9]+)\((.*)\) /private def \2(\3) : \1 = /g" {} \;

echo "replace 'private type variable = ' to 'private var variable : type = '"
find src -name "*.scala" -exec perl -p -i -e "s/private ([A-z0-9<>,]+) ([A-z0-9_]+) = /private var \2 : \1 = /g" {} \;
find src -name "*.scala" -exec perl -p -i -e "s/private ([A-z0-9<>,]+) ([A-z0-9_]+);/private var \2 : \1 = null/g" {} \;

echo "replace 'Class<T1, T2>' to 'Class<T1,T2>'"
find src -name "*.scala" -exec perl -p -i -e "s/<([A-z0-9]+)>/\[\1\]/g" {} \;
find src -name "*.scala" -exec perl -p -i -e "s/<([A-z0-9]+), ([A-z0-9]+)>/\[\1,\2\]/g" {} \;

# catch exception
echo "replace ' catch(.*Exception.*) ' to ' catch '"
find src -name "*.scala" -exec perl -p -i -e "s/\bcatch \(.*Exception.*\)/catch/g" {} \;

# println
echo "replace ' System.out.println ' to ' println '"
find src -name "*.scala" -exec perl -p -i -e "s/System.out.(print|println)/\1/g" {} \;

# method parameters
echo "replace 'def method(type parameter)' to ' def method(parameter:type) '"
find src -name "*.scala" -exec perl -p -i -e "s/(.* def .*)\(([A-z0-9\[\],]+) ([A-z0-9_]+)\)( |;)/\1(\3: \2) /g" {} \;
find src -name "*.scala" -exec perl -p -i -e "s/(.* def .*)\(([A-z0-9\[\],]+) ([A-z0-9_]+), ([A-z0-9\[\],]+) ([A-z0-9_]+)\)( |;)/\1(\3: \2, \5: \4) /g" {} \;
find src -name "*.scala" -exec perl -p -i -e "s/(.* def .*)\(([A-z0-9\[\],]+) ([A-z0-9_]+), ([A-z0-9\[\],]+) ([A-z0-9_]+), ([A-z0-9\[\],]+) ([A-z0-9_]+)\)( |;)/\1(\3: \2, \5: \4, \7: \6) /g" {} \;
find src -name "*.scala" -exec perl -p -i -e "s/(.* def .*)\(([A-z0-9\[\],]+) ([A-z0-9_]+), ([A-z0-9\[\],]+) ([A-z0-9_]+), ([A-z0-9\[\],]+) ([A-z0-9_]+), ([A-z0-9\[\],]+) ([A-z0-9_]+)\)( |;)/\1(\3: \2, \5: \4, \7: \6, \9: \8) /g" {} \;

# define variable
echo "replace 'type variable = value;' to 'var variable = value'"
find src -name "*.scala" -exec perl -p -i -e "s/^(\s+)([A-z0-9,\[\]]+) ([A-z0-9_]+) = (.*);$/\1var \3 = \4/g" {} \;
echo "replace 'type variable;' to 'var variable = null'"
find src -name "*.scala" -exec perl -p -i -e "s/^(\s+)([A-Z][A-z0-9,\[\]]*) ([A-z0-9_]+);$/\1var \3: \2 = null/g" {} \;

# for
echo "replace 'for (type variable : list) {' to 'for (variable <- list) {'"
find src -name "*.scala" -exec perl -p -i -e "s/^(\s+)for \(([A-z0-9,\[\]]+) ([A-z0-9_]+) : ([A-z0-9\.\[\]\(\)]+)\) /\1for (\3 <- \4) /g" {} \;


echo "comment all"
find src -name "*.scala" -exec perl -p -i -e "s/^/\/\//g" {} \;






