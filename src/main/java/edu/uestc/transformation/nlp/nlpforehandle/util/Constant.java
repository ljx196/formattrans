package edu.uestc.transformation.nlp.nlpforehandle.util;

/**
 * 常量
 *
 * @author leesia
 */
public interface Constant {
	/**
	 * 数值测试误差
	 */
	double ERROR = 0.1;
    //空字符串
    String STR_NULL = "";
    String STR_BLANK_SPACE = " ";

    String CUT_SYMBOL = "$";//语义同义化后删除的字符串

    String STUDENT_ANSWER_SPLIT = "#%#";
    String EQUATIONS_SPLIT = "#";
    String EQUATION_RIGHT = "=0";
    String EQUAL_SIGN = "=";

    //NER tag 和标签的分隔符号
    String NER_TAG_SPLIT = "&&";

    /**
     * 题目相关
     */
    String CHOICE = "choice";
    String FILL_IN = "fill_in";
    String SOLUTION = "solution";

    /**
     * 服务相关
     */
    String SERVICE  = "service";
    String SERVICE_UTIL  = "cn.tsinghuabigdata.util"; //根据代码
    String SERVICE_GEO_DRAW_JUNIOR = "cn.tsinghuabigdata.juniorGeoDraw";
    String SERVICE_GEO_UNCERTAIN_JUNIOR = "cn.tsinghuabigdata.juniorGeoUncertain";
    String SERVICE_GEO_UNCLOSED_SHAPE_JUNIOR = "cn.tsinghuabigdata.juniorGeoUnclosedShape";//
    String SERVICE_GEO_DRAW_GRAPH_JUNIOR = "cn.tsinghuabigdata.juniorGeoDrawGraph";//
    String SERVICE_GEO_DRAW_VERTICAL_JUNIOR = "cn.tsinghuabigdata.juniorGeoDrawVertical";//
    String SERVICE_GEO_DRAW_SYMMETRY_JUNIOR = "cn.tsinghuabigdata.juniorGeoDrawSymmetry";//
    String SERVICE_GEO_DRAW_PARALLEL_JUNIOR = "cn.tsinghuabigdata.juniorGeoDrawParallel";//
    String SERVICE_GEO_DRAW_LINE_CIRCLE_JUNIOR = "cn.tsinghuabigdata.juniorGeoDrawLineCircle";//
    String SERVICE_NEST = "cn.tsinghuabigdata.nest";

    /**
     * 模板相关
     */
    String XML_PATH_COMPOSITE = "template/composite_template.xml";
    String XML_PATH_FUNCTION = "template/function_template.xml";
    String XML_PATH_ALGEBRA = "template/algebra_template.xml";
    String XML_PATH_GEO_CLOSED_SHAPE = "template/geo_closed_shape_template.xml";
    String XML_PATH_GEO_DRAW = "template/geo_draw_template.xml";
    String XML_PATH_GEO_UNCERTAIN = "template/geo_uncertain_template.xml";
    String XML_PATH_GEO_UNCLOSED_SHAPE = "template/geo_unclosed_shape_template.xml";
    String XML_PATH_GEO_DRAW_GRAPHICS_TRANSFORMATION = "template/geo_draw_graphics_transformation_template.xml";
    String XML_PATH_GEO_DRAW_VERTICAL = "template/geo_draw_vertical_template.xml";
    String XML_PATH_GEO_DRAW_SYMMETRY = "template/geo_draw_symmetry_template.xml";
    String XML_PATH_GEO_DRAW_PARALLEL = "template/geo_draw_parallel_template.xml";
    String XML_PATH_GEO_DRAW_LINE_CIRCLE = "template/geo_draw_line_circle_template.xml";
    String XML_PATH_SOLID_GEO = "template/solidGeo_template.xml";
    String XML_PATH_NEST = "template/nest_template.xml";


    //test
    String XML_PATH_GEO_DRAW_OLD = "template/geo_draw_old_template.xml";
    String XML_PATH_LINE = "template/line_template.xml";
    String XML_PATH_POINT = "template/point_template.xml";
    String SYNSET = "template/synset_template.xml";

    /**
     * v3 xml 相关
     */
    String XML_RULE_ERROR = "xml/ruleserror.xml";
    /**
     * 汉字形状
     */
    String CHINESE_PARALLELOGRAM = "平行|平行四边形";//
    String CHINESE_TRAPEZOID = "梯形";
    String CHINESE_IN_CIRCLE = "圆内|内";
    String CHINESE_OUT_CIRCLE = "圆外|外";
    String CHINESE_ISOSCELES_TRAPEZOID = "等腰|等腰梯形";
    String CHINESE_RECTANGULAR = "矩形|长方形";
    String CHINESE_DIAMOND = "菱形";
    String CHINESE_RIGHT_ANGLE_TRAPEZOID = "直角|直角梯形";
    String CHINESE_SQUARE = "正方形|□";
    String CHINESE_TRIANGLE = "三角形|△";
    String CHINESE_TRIANGLE_ALL = "等边|等腰|正|直角|Rt|R|等腰直角|钝角|锐角";
    String CHINESE_FUNCTION_NAME = "双曲线|一次函数|函数";
    String CHINESE_QUADRILATERAL = "四边形";
    String CHINESE_QUADRILATERAL_ALL = "四边形|梯形|等腰梯形|直角梯形|Rt梯形|平行四边形|平行□|矩形|菱形|正方形|□";
    String CHINESE_POINT = "点";
    String CHINESE_ISOSCELES_RIGHT_TRIANGLE = "等腰直角|等腰Rt|等腰直角三角形";
    String CHINESE_ISOSCELES_TRIANGLE = "等腰|等腰三角形";
    String CHINESE_RIGHT_TRIANGLE = "直角|Rt|R";
    String CHINESE_EQUILATERAL_TRIANGLE = "正|等边|正三角形|等边三角形";
    String CHINESE_ACUTE_TRIANGLE = "锐角|锐角三角形";
    String CHINESE_OBLIQUE_TRIANGLE = "斜|斜三角形";
    String CHINESE_OBTUSE_TRIANGLE = "钝角|钝角三角形";
    String CHINESE_CONGRUENT_TRIANGLE = "≌|全等";
    String CHINESE_DIRECTION_UP = "上";
    String CHINESE_DIRECTION_DOWN = "下";
    String CHINESE_DIRECTION_LEFT = "左";
    String CHINESE_DIRECTION_RIGHT = "右";
    String CHINESE_AREA = "面积";
    String CHINESE_ANGLE = "角";
    String CHINESE_BOXER_ANGLE = "平角";
    String CHINESE_ROUND_ANGLE = "周角";
    String CHINESE_RIGHT_ANGLE = "直角|Rt|R";
    String CHINESE_ACUTE_ANGLE = "锐角";
    String CHINESE_OBTUSE_ANGLE = "钝角";
    String CHINESE_SHAPE_CIRCLE_SCRIBED = "内接|外接";
    String CHINESE_SHAPE_CIRCLE_CIRCUMSCRIBED = "内切|外切";
    String CHINESE_INSCRIBED = "内切";
    String CHINESE_CIRCUMSCRIBED = "外切";
    String CHINESE_INNER_CENTRE = "内心";
    String CHINESE_CIRCUM_CENTRE = "外心";
    String CHINESE_GRAVITY_CENTRE = "重心";
    String CHINESE_ORTHO_CENTRE = "垂心";
    String CHINESE_ALTERNATE = "内错角";
    String CHINESE_SAMEINETERIOR = "同旁内角";
    String CHINESE_CORRESPONDING = "同位角";
    String CHINESE_VERTICAL = "对顶角";
    String CHINESE_SUPPLEMENT = "补角|补";
    String CHINESE_COMPLEMENT = "余角|余";
    String CHINESE_STRAIGHT_LINE = "一次函数|直线|一次|正比例函数|正比例";
    String CHINESE_PIECEWISE = "分段|分段函数";
    String CHINESE_PARABOLA = "一元二次方程|抛物线|二次函数";
    String CHINESE_INVERSE_PROPORTION = "反比例|双曲线";
    String CHINESE_ANTICLOCKWISE = "逆时针";
    String CHINESE_CLOCKWISE = "顺时针";
    String CHINESE_X_AXIS = "x轴|X轴";
    String CHINESE_Y_AXIS = "y轴|Y轴";
    String CHINESE_ASSUME = "设|假设";
    String CHINESE_NUMBER = "一|二|三|四|五|六|七|八|九|十|两";
    //如果有包含关系 长的字符串写在前面
    String CHINESE_ORIGIN_FUNCTION_EXPR = "若|求代数式|求值|原方程组|原不等式组|方程组|不等式组|原方程|原不等式|方程|不等式(组|)|化简|计算|求(式子|)|代数式|分式|合并同类项|去括号|分数|因式分解|分解因式|幂|各式|约分";
    String FUNCTION_ANAPHJOA = "解析式";
    /**
     * 转关系相关
     */
//	String JSON_TO_RELATION = "JsonToRelation";//转关系反射函数名称
    String JSON_TO_RELATION_PACKAGE = "com.tsinghuabigdata.edu.mathengine.nlu.jsontorelation.";
    String JSON_TO_DATA_PACKAGE = "com.tsinghuabigdata.edu.mathengine.nlu.jsontodata.";

    /**
     * Json 主要字段
     */
    String JSON_CODE = "code";                        //Json  反射类名
    String PROCESS_DATA = "processData";            //Json中的后处理数据 字段名称
    String INSTEAD_OF_DATA_ORIGIN = "insteadOfDataOrigin";    //代入关系的后处理数据, 代入前
    String INSTEAD_OF_DATA_SUBS = "insteadOfDataSubs";    //代入关系的后处理数据，将要代入的数据
    String INSTEAD_OF_DATA_CONCLUSION = "insteadOfDataConclusion";                //代入关系的后处理数据,代入结论
    String DATA = "data";                            //Json中的转关系数据 字段名称
    String SENTENCE = "sentence";                    //文字关系的内容
    String POINT = "point";                            //点
    String PROJECTION = "projection";       //射影
    String CONTAIN_END_POINT = "containEndPoint";//是否包含结束点
    String QUADRANT = "quadrant";                            //象限
    String ANGLE = "angle";                            //角
    String SECTOR = "sector";                        //扇形
    String SECTOR_TYPE = "sectorType";                //扇形类型
    String ANGLE_TYPE = "angleType";                //角类型
    String TRIANGLE = "triangle";                    //三角形
    String TRIANGLE_TYPE = "triangleType";            //三角形类型
    String CIRCLE_ANGLE_TYPE = "circleType";        //圆角类型
    String CIRCLE = "circle";                        //圆
    String CENTER="center";
    String OUT_CIRCLE = "outCircle";                //外圆
    String IN_CIRCLE = "inCircle";                    //内圆
    String LINE = "line";                            //线
    String LINES = "lines"; 
    String OUT_EXTEND_LINE = "outExtendLine";		//延长线外取一点
    String SYMMETRIC = "symmetric";
    String CHORD = "chord";
    String INTERSECT_LINE = "intersectLine";        //交线
    String INTERSECT_POINT = "intersectionPoint";    //交点
    String LINE_TYPE = "lineType";                    //线的类型
    String AXIS = "axis";                            //坐标轴
    String ARC = "arc";                                //弧
    String ARC_TYPE = "arcType";                    //弧类型
    String QUADRILATERAL = "quadrilateral";            //四边形
    String POLYGEN="polygen";						//多边形
    String REGULAR="regular";
    String SPHERE = "sphere";                                       //球
    String QUADRILATERAL_TYPE = "quadrilateralType";//四边形类型
    String RELATION_TYPE = "relationType";            //关系类型
    String POINT_POSITION = "pointPosition";        //点的位置
    String POSITION = "position";
    String CONNECT_TYPE = "connectType";            //圆相切类型外切内切相切
    String CONNECTION_TYPE = "connectionType";        //封闭图形和圆的外切内切 内接外接
    String DIRECTION = "direction";                    //方向
    String OPENDIRECTION = "opendirection";                    //开口方向
    String TRANSLATE_INFO_LIST = "translateInfoList"; //平移信息data集合
    String DECLINATION = "declination";                //方位角的单位
    String HEIGHT = "height";                        //高
    String BOTTOM = "bottom";//底边
    String parallelogram = "parallelogram";  //平行四边形
    String TRAPEZOID = "trapezoid";                    //梯形
    String TRAPEZOID_TYPE = "trapezoidType";        //梯形类型
    String SOLVE_FEATURE = "solveFeature";            //方程组的解集特征
    String SOLVE = "solve";                            //方程组的解集
    String EQUATION_SYSTEM_DATA = "equationSystemData";                //方程组解关系中: 原方程组
    String EQUATION_SOLUTION = "solution";                            //方程组的解集
    String NO_SOLUTION = "noSolution";                                //方程无解
    String EXTRANEOUS="extraneous";                                   //曾根
    String EQUATION_SOLUTION_FEATURE = "feature";                            //方程组的解集特征
    String EQUATION_SOLUTION_EXPR = "expr";                            //方程组的解集
    String EQUATION_SOLUTION_COMPARE = "expressCompare";                //方程组解的特征
    String OPERATOR_EXPR = "operatorExpr";                //方程两边操作的原始方程
    String OPERATE_TYPE = "operateType";                    //方程两边操作的操作符号：乘 减等
    String RESULT_EXPRESS = "resultExpress";                //方程两边操作的结果方程
    String UNKNOWN_RESULT_EXPRESS = "unknownResultExpress";                //方程两边操作的结果方程 未知，后处理补上
    String CASTING_OUT = "castingOut";                    //舍去
    String CONDITION = "condition";                                //当条件
    String CONDITION_LIST = "conditionList";                                //满足条件
    String EXIST_LIST = "existList";                                //满足关系的存在表达式
    String SATISFY_LIST = "satisfyList";                                //满足的表达式
    String CONCLUSION = "conclusion";                                //当结论
    String DUE_TO_LIST = "dueToList";                            //由于关系 原表达式
    String OBTAIN_LIST = "obtainList";                            //由于关系的结论
    String ASSUME_RELATION_DATA = "assumeRelationData";                            //设关系数据
    String EXPR_ALL_RELATION_CODE = "algebrarelation.JsonDealExprTagRelation";
    String EXPR_ONLY_RELATION_CODE = "algebrarelation.JsonExpressRelation";//表达式
    String CONCLUSION_SAVE = "conclusionSave"; //保存结论的表达式
    String HEART = "heart";
    String SHAPE = "shape";
    String SHAPE_TYPE = "shapeType";//确定封闭图像, 比如是三角形还是四边形
    String SHAPE_STYLE = "shapeStyle";//确定封闭图像的具体类型 比如是正方形或者长方形
    String GRAPHICS_BEGIN = "graphicsBegin";//旋转 平移前的图像
    String GRAPHICS_END = "graphicsEnd";//旋转 平移后的图像
    String GRAPHICS_TYPE = "graphicsType";//函数类型 旋转 平移图像类型
    String ROTATE_UNIT = "rotateUnit";//平移的单位
    String ROTATE_DEGREE = "rotateDegree";//旋转的度数
    String OR_RELATION_DATA = "orRelationData";//或关系的参数字段
    String AND_RELATION_DATA = "andRelationData";//或关系的参数字段
    String INDETERMINACY_RELATION_DATA = "indeterminacyRelation";//不确定关系参数
    String NEGATIVE_RELATION_DATA = "negativeRelation";//否定关系参数
    String DIVISORED_EXPRESS = "divisoredExpress";
    String DIVISOR_EXPRESS = "divisorExpress";//除数
    String TYPE = "type";//类型
    String PROPORTION = "proportion"; //比例
    String AREA_OR_PERIMETER = "areaOrPerimeter";//面积或周长
    String GRAPHICS = "graphics";//图形名称
    String EQUATION_VAR = "equationVar";//方程变量
    String VAR="var";          //自变量
    String EQUATION_DEGREE = "equationDegree";//方程的次数
    String EQUATION_VAR_NUM = "equationVarNum";//方程变量个数
    String EQUATION_FEATURE = "equationFeature";//方程变量个数
    String FUNCTION_NAME = "functionName";//函数名字
    String FUNCTION = "func";
    String INDEPENDENT_VAR = "independentVar";//自变量
    String DEPENDENT_VAR = "dependentVar";//因变量
    String FUNCTION_TYPE = "functionType";//函数类型
    String SYMMETRY_AXIS = "symmetryAxis";//对称轴
    String VERTEX = "vertex";
    String MAX_VALUE = "maxValue";
    String MIN_VALUE = "minValue";
    String MIN_PERIODIC = "minPeriodic";
    String POLYHEDRON = "polyhedron";
    String PLANE = "plane";
    String ROTATOR = "rotator";
    String SLOPE = "slope";
    String DEGREE = "degree";
    String SOR = "source";
    String ANALYTIC = "analytic";
	String POSSIBLE_VALUE_RELATION_DATA = "possibleValue";
	String INTERVAL_RELATION_DATA = "intervalValue";
	String DEFAULT_RELATION_DATA = "defaultValue";
    String SEQUENCE = "sequence";
    String SUBSCRIPT = "subscript";
    String NUM_LIST = "numList";
    String INDEX="index";
    String NUMBER="num";
    String TARGET="target";
    String first="first";
    String second="second";

    String CONIC_CURVE = "conicCurve";
    String SEQUENCE_DATA = "sequenceData";
    String CONIC_CHARA = "conicCharacter";
    String STRAIGHTLINE = "straightLine";
    String CURVE = "curve";
    String VEC = "vector";
    String CAUSAL_RELATION_DATA = "causalRelationData";
    /**
     * 表达式相关字段
     */
    String EXPR = "expr";
    String EXPR_X = "exprX";
    String EXPR_Y = "exprY";
    String EXPR_KEY_WORD = "exprKeyWord";
    String EXPR_ORIGINAL = "原式";
    String EXPR_FUNCTION = "方程|原方程";
    String EXPR_ORIGINAL_JSON = "原式=";
    String EXPR_ORIGINAL_JSON_ERROR = "原式==";
    String EXPR_RELATION_CODE_NAME = "algebrarelation.JsonExpressRelation";
    String LINE_DIRECTION = "lineDirection";//直线的方向
    String UNIT = "unit";//表达式单位
    String MISSING_TERM_LIST = "missingTermList";//不含有元素项集合
    String VEC_SYMBOL = "↑";
    String CONSTANT = "constant";
    String EQUALITY = "equality";
    String WILD_POINT="wild";   //一个点如果某一个方向的坐标未知，则用此表示。
    /**
     * 作图相关字段
     */
    String DRAW_POINT = "drawPoint";                        //后处理新点字段
    String DRAW_PACKAGE_CALCULATE = "com.tsinghuabigdata.edu.mathengine.nlu.allpostprocessing.drawprocessing.draw.calculate.";    //code的包名
    String DRAW_CALCULATE_FUNCTION_NAME = "calculateCoordinate";    //计算反射的函数名称
    //后处理字段中的，变量字段
    String UNKNOWN_POINT = "unknownPoint";                            //垂直关系中的未知垂点
    String DRAW_INTERSECTION_POINT = "intersectionPoint";        //垂直关系中Json中的垂点字段
    String DRAW_THROUGH = "through";
    String DRAW_LINE = "drawLine";
    String TANGENT_POINT = "tangentPoint";                    //切点
    String DRAW_INTERSECT_LINE = "intersectLine";
	String DRAW_VERTICAL_LINE = "verticalLine";			//垂线
	String DRAW_VERTICAL_POINT = "verticalPoint";	
    String DRAW_CIRCLE_COLLINEAR = "drawCircleCollinear";        //更新共园信息
    String DRAW_CIRCLE_COLLINEAR_ADD_POINT = "add";        //更新共园信息 新点
    String DRAW_CIRCLE_COLLINEAR_CIRCLE = "circle";        //更新共园信息 圆
    String DRAW_POINT_COLLINEAR = "drawPointCollinear";//更新共线信息
    String DRAW_POINT_COLLINEAR_ADD_POINT = "add";//更新共线信息
    String DRAW_POINT_COLLINEAR_ORIGIN = "origin";//原始线
    String DRAW_PARALLEL_LINE = "parallelLine";//平行线
    String CROSS_POINT = "crossPoint";//交点
    String SYMMETRY_LINE = "symmetryLine";//对称线
    String SYMMETRY_POINT = "symmetryPoint";//对称点

    /**
     * 直线指代 字段名称
     */
    String LINE_REFERENCE_SAVE = "line_reference_save";
    String LINE_REFERENCE_SAVE_KEY = "key";
    String LINE_REFERENCE_SAVE_VALUE = "value";
    String LINE_REFERENCE_GET = "line_reference_get";
    String LINE_REFERENCE_GET_NAME = "name";
    String LINE_REFERENCE_GET_VALUE = "value";
    String LINE_REFERENCE_NEW_CODE = "newcode";

    /**
     * 圆指代 字段名称
     */
    String POINT_REFERENCE_SAVE = "point_reference_save";
    String POINT_REFERENCE_SAVE_KEY = "key";
    String POINT_REFERENCE_SAVE_VALUE = "value";
    String POINT_REFERENCE_GET = "point_reference_get";
    String POINT_REFERENCE_GET_NAME = "name";
    String POINT_REFERENCE_GET_VALUE = "value";

    /**
     * 圆指代 字段名称
     */
    String CIRCLE_REFERENCE_SAVE = "circle_reference_save";
    String CIRCLE_REFERENCE_NEW_CODE = "newcode";
    String CIRCLE_REFERENCE_GET = "circle_reference_get";
    String CIRCLE_REFERENCE_GET_NAME = "name";
    String CIRCLE_REFERENCE_GET_VALUE = "value";
    String CIRCLE_REFERENCE_SAVE_KEY = "key";
    String CIRCLE_REFERENCE_SAVE_VALUE = "value";

    /**
     * 直径半径指代
     */
    String RAD_DIA_REFERENCE_SAVE = "rad_dia_reference_save";
    String RAD_DIA_REFERENCE_SAVE_KEY = "key";
    String RAD_DIA_REFERENCE_SAVE_VALUE = "value";
    String RAD_DIA_REFERENCE_GET = "rad_dia_reference_get";
    String RAD_DIA_REFERENCE_GET_NAME = "name";
    String RAD_DIA_REFERENCE_GET_VALUE = "value";
    String RAD_DIA_REFERENCE_NEW_CODE = "newcode";


    /**
     * 图像后处理 指代
     */
    String GRAPH_REFERENCE_SAVE = "graph_reference_save";
    String GRAPH_REFERENCE_GET = "graph_reference_get";
    String GRAPH_REFERENCE_SAVE_KEY = "key";
    String GRAPH_REFERENCE_SAVE_VALUE = "value";
    String GRAPH_REFERENCE_GET_NAME = "name";
    String GRAPH_REFERENCE_GET_VALUE = "value";
    String GRAPH_REFERENCE_GET_NEW_CODE = "newcode";
    String GRAPH_FUNCTION_REGEX = "该方程|此方程|图像|它";
    String GRAPH_FUNCTION_REGEX_SAVE = "一元二次方程|直线";

    /**
     * 方程指代后处理
     */
    String EQUATION_REFERENCE_SAVE = "equation_reference_save";
    String EQUATION_REFERENCE_SAVE_KEY = "key";
    String EQUATION_REFERENCE_SAVE_VALUE = "value";
    String EQUATION_REFERENCE_GET = "equation_reference_get";
    String EQUATION_REFERENCE_GET_NAME = "name";
    String EQUATION_REFERENCE_GET_VALUE = "value";
    String EQUATION_REFERENCE_GET_NEW_CODE = "newcode";
    String Root_Prefix = "x_";
    /**
     * 表达式相关的标记
     */
    String System_Flag = "#";
    String Propertion_Flag = ":";

    /**
     * 配置文件后处理相关 字段名称
     */
    String METHOD_NAME = "methodName"; //查找配置文件所调用方法的名称
    String FIND_CIRCLE_CONFIG = "find_circle_config";
    String FIND_PERPENDICULAR_POINT_CONFIG = "find_perpendicular_point_config"; //通过配置文件查找垂点
    String FIND_CONFIG_POINT = "point";
    String FIND_CONFIG_NAME = "name";
    String FIND_CONFIG_VALUE = "value";


    //正则表达式
    String REGEX_NUMBER = "[0-9]+";        //数字
    String AllOperator = "(\\+|-|\\*|/)";
    String SetCompareOperator = "(⊆|⊇|⊂|⊃|⊊|⊋)";
    String EQUATION_REFER = "((\\{|)(①|②|③|④|⑤|⑥|⑦|⑧|⑨|⑩|⑪|⑫|⑬|⑭|⑮|⑯|⑰|⑱|⑲|⑳)(\\}|))";
    String POS_SAVE = "n|nh|ns|ni|nt|nl|m|q|nz";
    String POS_UN_KEYWORD = "u|wp";
    String SYMBOL = "(,|\\.)";
    String EXPRESS_KEY_WORD = "(≥|>|＜|≤|<|≠|=|≈)";
    String Graph = "(△|□|⊙)";
    String areaOrPerimeter = "(S_|C_)";
    String area = "S_";
    String perimeter = "C_";
    String IN = "in";
	String NEGATIVE_REGULAR = "(不(?=(在|是|为|经过|相似|∽|全等|≌|共线|相交|相∩|平行|∥|垂直|⊥))|(?<!个)不(?=(相等|=))|不等(?!式)(?=都)不)";
    String PROVE_REGULAR = "(?<!(，|,))(证明|求证|试证明(:|：))";
    /**
     * 匿名对象的名称
     */
    String ANONYMOUS = "*";
    /**
     * 结论
     */
    String PROVE_RELATION_DATA = "proveRelationData";
    String COMPUTE_VALUE = "computeValue";
    String TRANS_RELATION_DATA = "transRelationData";
    String RELATION_CHARACTER = "relationCharacter";
    String STATISTICS_CHARACTER = "statisticsCharacter";
    String RELATION = "relation";
    String ELEMENT = "element";
    String SET = "set";
    String RANGE = "range";
    String SPECIALSET = "specialSet";
    String CAUSAL_TYPE = "causalType";
    String RADIUS_LENGTH = "radiusLength";
    String ALIQUOTS = "aliquots";
    String AREA_NAME = "areaName";
    String NAME = "name";
    String LENGTH = "length";
    String PARTY = "party";
    String VALUE = "value";
    String FIELD = "field";
    
	//结论计算类型
	String Compute_value = "值";
    String proveConclusion = "proveConclusion";
    
    
    //应用题
	String ENTITY="entity";
	String AFTERENTITY="afterentity";
	String NER_TAG_EXPR="expr";
}
