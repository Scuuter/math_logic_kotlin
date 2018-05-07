.PHONY: all compile run pack clean out -include-runtime

SOURCES=src/*.kt
MAINCLASS = MainKt

all:	prepare-kotlin out compile

run:
	java -jar hello.jar

pack:
	zip hw1.zip -r Makefile src kotlin.mk

clean:
	rm -rf out
	rm hello.jar

compile:
	${KOTLINC} ${SOURCES} -include-runtime -d hello.jar $

out:
	mkdir -p out

include kotlin.mk