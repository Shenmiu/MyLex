package mylex.LexAnalyzer;

import mylex.LexAnalyzer.dfa.DFA;
import mylex.LexAnalyzer.dfa.DFAOptimizer;
import mylex.LexAnalyzer.nfa.NFA;
import mylex.LexAnalyzer.patternProcessor.PatternProcessor;
import mylex.vo.Pattern;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LexAnalyzer {

    private static Logger logger = Logger.getLogger(LexAnalyzer.class.getName());

    /**
     * 根据传入的模式，构造一个基于优化后的DFA词法分析器
     * @param patterns 解析.l文件后的所有模式
     * @return 词法分析器
     */
    public Tokenizer createTokenizer(List<Pattern> patterns){
        logger.info("开始解析pattern");
        PatternProcessor patternProcessor = new PatternProcessor(patterns);
        logger.info("解析pattern完成");
        logger.info("开始构建NFA");
        List<NFA> nfaList = patternProcessor.combinePatterns();
        logger.info("NFA构建完成");

        List<DFA> dfaList = new ArrayList<>();
        for (int i = 0; i < nfaList.size(); i++) {
            logger.info("开始构建DFA");
            DFA dfa = new DFA(nfaList.get(i));
            logger.info("DFA构建完成");
            logger.info("开始优化DFA");
            DFAOptimizer dfaOptimizer = new DFAOptimizer(dfa.Dtran());
            logger.info("DFA优化完成");
            dfaList.add(dfaOptimizer.constructOptimizedDFA());
        }

        return new Tokenizer(dfaList);
    }

}
