/**
 * Copyright 2017 The MWG Authors.  All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ml.classifier;

/**
 * Created by andrey.boytsov on 17/05/16.
 */
/**
 * Created by Andrey Boytsov on 4/18/2016.
 */

import org.junit.Test;
import org.mwg.*;
import org.mwg.internal.scheduler.NoopScheduler;
import org.mwg.ml.AbstractMLNode;
import org.mwg.mlx.MLXPlugin;
import org.mwg.mlx.algorithm.AbstractClassifierSlidingWindowManagingNode;
import org.mwg.mlx.algorithm.classifier.LogisticRegressionClassifierNode;

import static org.junit.Assert.assertTrue;

/**
 * Created by Andrey Boytsov on 4/18/2016.
 */
public class LogisticRegressionTest extends AbstractClassifierTest{
    //TODO Changing parameters on the fly

    private static final double f[] = new double[]{7.308781907032909,
            4.100808114922017, 2.077148413097171, 3.3271705595951118, 9.677559094241207, 0.061171822657613006, 9.637047970232077,
            9.398653887819098, 9.471949176631938, 9.370821488959697, 3.971743421847056, 3.4751802920311023, 2.9405703200403677,
            5.064836273262351, 1.1596708803265776, 7.705358800791777, 6.5989270869342, 1.5674689056984625, 3.782020453210553,
            1.3976268290375116, 6.949479796024919, 8.052277714737137, 0.050251759924525574, 5.231351557883331, 7.439844862373166,
            1.4202270321592614, 4.817283015755979, 5.445548088936737, 5.771002613742765, 2.0491354575856158, 6.233638106316658,
            1.8470709027794863, 0.10684410982228831, 1.6104332337170224, 1.7805484663717475, 5.403970647837965, 9.738340099187003,
            2.454265535812956, 3.945209062942218, 2.1760212490768485, 4.320068798935392, 2.331557947513334, 8.89908751655377,
            0.3832689331927541, 5.923792438629388, 6.551736031294164, 1.1983904230418962, 6.524767068959532, 9.843228006692948,
            2.0673750736050733, 3.7464950029397013, 4.633497875694758, 3.336102111746697, 4.432124503172691, 5.041355667045739,
            9.989808147679287, 6.304048639228538, 9.095840355890243, 5.076599160699481, 4.914512055616126, 4.287841787462007,
            3.0809291520847983, 7.173224016222269, 9.624145927337464, 2.0949273346356434, 1.7264882091400524, 5.489525169306556,
            5.554683820879616, 5.878158289013334, 7.891906578350958, 6.9900662243448055, 2.0463555220568552, 2.549094512998157,
            7.7791635951952784, 2.2502238600837545, 9.83103184833703, 8.03618606015427, 8.363149477563287, 1.6310352738831468,
            6.374974298594064, 0.08760417318731673, 6.311898883115249, 2.079779790426225, 8.803572849070424, 7.070442260539652,
            7.233716462158025, 5.094929196703692, 9.871021380962999, 1.5324623281626104, 7.105747751483344, 8.239808754531598,
            1.2367122850292422, 6.169513867462899, 4.818623552605982, 0.9975028132571628, 6.171458898614318, 0.28120372951167205,
            5.914391059546381, 0.7302602021384397, 0.7404792727334575, 6.79935420191025, 2.8000161578266605, 6.039836876835673,
            8.496408070142763, 3.538232827704746, 5.986324882746571, 2.9209341135625424, 8.764426533244832, 7.442496201360502,
            4.232327288662894, 3.7580820874305876, 2.7129332517471583, 7.298239289350995, 8.867444223713157, 0.43702698296824716,
            4.5770018307350995, 8.624497547615505, 9.019239557423012, 0.6248845991277674, 7.638204881746642, 4.2770008141825855,
            9.991834307905936, 0.619018022786263, 5.863206319929349, 5.67293070903705, 6.620259613053776, 9.072811901181929,
            2.4648563130230903, 2.484887670873126, 7.068583347506787, 3.3772092726397043, 6.073350789607563, 3.154227864574829,
            0.24472129835244494, 4.510085026154496, 3.6913431550132705, 4.638377596597093, 1.7724271181295093, 6.761231545253681,
            7.155724849913588, 7.433696227652655, 9.291026104275344, 5.898935294704389, 6.4401228738355245, 1.104898534174854,
            7.746041094589238, 3.0648278810533993, 8.805986697893163, 6.3040144078499125, 9.288937133245545, 1.9668940091077458,
            7.546462452610667, 1.576306138076371, 0.4217868395172397, 3.786228753606087, 7.866092457818081, 7.127521677660935,
            1.102736115092493, 7.621330697392812, 0.30068835878899747, 3.1663147897269206, 6.670733531962961, 4.26443047409429,
            4.005571613130963, 7.437615476135019, 3.653057377846176, 7.637548990718225, 3.226691910455337, 5.127188233824031,
            4.786758722065944, 1.3116881312494189, 0.3179647698528931, 4.444566110953367, 6.915212116814827, 7.190756853468042,
            7.77950272380208, 2.88100716049866, 7.813547336294311, 9.662539506822094, 9.965234515119386, 2.3910643632747064,
            4.451812617337756, 1.302219280116259, 2.344740875050564, 6.560824389883995, 5.7222959250588925, 5.559666311129164,
            2.502962555948841, 9.20281192548133, 7.237512084322349, 7.76990965679351, 2.4227614275862077, 1.7570195450010273,
            4.447940059612577, 8.296912233491993, 6.019146360600576, 6.379468960547, 3.033671447590315, 0.6539702112886914,
            9.3803050123258, 2.664167073273985, 2.29171358367752, 3.0397643756233075, 3.7194051348653154, 8.571270426792013,
            8.070763655241631, 3.8872927575292127, 2.6524646103095018, 0.3237285340092644, 6.6978526348207845, 0.7436137629811046,
            6.09141081433683, 1.7866235941017028, 0.2963321139734998, 7.08153827940544, 2.777782747076535, 1.157884427761261,
            6.912259325002928, 3.490107239093957, 2.281355022594692, 1.6759999599955966, 5.946311329871144, 8.098898289838598,
            5.149291408485576, 3.4898138772155587, 0.5309346425588812, 6.061902042033665, 6.815264621608791, 5.837433172059664,
            3.6975249929974696, 9.459920309753782, 6.864305832800729, 2.2457387021115425, 4.445994347448496, 7.0761354425454135,
            0.29797717682461533, 6.588936239634658, 3.159175147233838, 8.15770241451412, 1.5557863513492431, 4.534042358503293,
            3.197081668867078, 5.496925287865534, 5.222592465012812, 3.1531501628136303, 6.961128822508109, 0.9487740515771204,
            9.588080550158974, 1.2967758890382586, 0.23598618121072001, 0.8472082428015759, 0.523207845569158, 4.699535995042926,
            8.196823891995006, 9.6909724727718, 3.4998568701436703, 2.7288644092906242, 0.112285275189965, 7.940233434749962,
            1.0037536978542638, 8.189424032001662, 7.4785980727333765, 3.7986308673705285, 1.0770436318776633, 7.660327692439177,
            2.0878161844762944, 9.451020901929118, 0.2757389059275095, 7.469231770524976, 6.363258883437723, 9.385386123743867,
            0.9385852730802446, 9.596822491367917, 8.187796102262062, 2.148099347753689, 8.337721028642607, 2.5571629146356956,
            6.405704810942022, 7.16492282731302, 8.017828816275877, 4.613827089519154, 1.1963981246009647, 5.105833271180019,
            7.42931210503477, 0.5687004880600954, 5.598410954199078, 3.6799637322681065, 8.82235198222148, 8.799772068792894,
            0.6045988412760983, 0.7176552425471794, 2.2925325722985237, 2.226207298867815, 6.454591110906983, 2.929819962005631,
            8.855046786130782, 5.443435774165437, 7.018109259870454, 1.8305119791334112, 3.473327599047381, 0.3979437608735614,
            2.4928496501916895, 7.966813038398515, 7.079271000862475, 4.660915541372565, 8.033199444863032, 0.8208400644580893,
            4.7960557034512155, 9.611818478109091, 2.3811349128998858, 4.53188192034624, 4.810877822329298, 0.6574838541045613,
            1.0882339963492693, 8.932382871275491, 8.64376433147161, 7.989181522497233, 3.4978060673208544, 1.2996863321546248,
            8.919939623067634, 4.319648320167988, 1.2987253974477075, 6.696159391389869, 5.17706382941291, 5.543976034553895,
            9.167628663664386, 3.803520828791469, 1.7249759057073433, 1.3327217273350478, 4.783721386416097, 8.752899735987773,
            4.124412921387338, 5.292556661639499, 6.3305068663057105, 1.6103499099921081, 2.462498436554992, 8.519218162609063,
            0.4913385234394907, 8.898258680169368, 0.2766263063480445, 6.119490836114814, 2.9007696187812804, 0.8162779597088821,
            7.714686576756621, 3.593386447515857, 6.031252520178065, 3.671121046799218, 6.31842068903811, 8.844653568590997,
            6.574820434179904, 6.432766185523678, 0.6780052697125838, 6.027195010504439, 5.521959960728839, 5.305308440713138,
            9.129808145875817, 9.436928852902403, 9.344963074024069, 1.733910707046602, 1.655576541542202, 9.223077884444852,
            9.960622801652868, 7.365003587093165, 8.1901503834094, 5.819829326567416, 4.477217800174952, 4.1350888270193265,
            4.656121071090132, 6.688756322835598, 3.1233079700420996, 8.884201859553077, 7.217686739169311, 7.214348251941366,
            5.21093845119918, 9.673294861209333, 7.482275992406374, 7.409449075469806, 8.503435309922656, 5.330504538112962,
            2.6773494080453997, 7.4643553873209845, 6.938043761132161, 0.6594689100291251, 5.191108689941513, 8.112781719353185,
            5.296246866520944, 7.2135647982155815, 5.289676136313823, 6.289848652536058, 7.119676673848918, 3.57313748927502,
            9.309632972906508, 2.6217089051868148, 3.937107554939688, 9.658499257329497, 3.1736342389701386, 4.913374573898074,
            9.69810142019274, 6.769675437889874, 7.688143887265757, 0.9907626719226559, 4.201451916403886, 8.764579135029733,
            7.756168045138527, 5.004932169756319, 3.2616523345900292, 0.0705024322089165, 2.3741816496320878, 3.620936740897073,
            1.3741899143501635, 3.069509904293249, 6.081283999742628, 2.3906679716190347, 4.720549021179022, 0.8065270263659219,
            5.389938651852385, 8.048523294811545, 8.496382077676982, 0.9396175877193202, 5.6514823224583, 9.47067959880486,
            2.751385513187261, 3.3851493876505154, 4.795920840116413, 8.603210290420725, 5.196997613981448, 7.206246925627586,
            0.43435042070922525, 2.791236550157686, 7.768110196253884, 1.9511367640809385, 5.9236770448484855, 1.6307545435982906,
            6.117568854838211, 4.868691581400654, 9.167131353568266, 4.716734811226279, 7.67730793922769, 8.211683970570157,
            5.160407341697132, 1.4755445215994234, 4.437553346266209, 0.18396287762270203, 9.592076508355186, 0.6466929718662862,
            1.1755950191551656, 8.995969280165149, 6.1682645414230475, 4.918359513022691, 8.508216275886507, 1.9637117894571332,
            4.755397649438791, 5.723106378160226, 2.8469419347739144, 0.619521955544956, 0.9211602615295178, 4.264645294272059,
            2.102845369480859, 8.398618958959204, 0.0026304067605464887, 7.473468727246582, 6.817734918743113, 9.623710093623833,
            0.44188841083690256, 1.7786127395043716, 1.2911132581341789, 7.230560080691863, 1.8032213088496751, 3.334777554447792,
            8.227389827316095, 1.9606644969043951, 2.622378210397611, 0.6584743519624359, 3.445394903816447, 7.592864775102005,
            2.0371505023525973, 4.503830711608291, 4.272329441204111, 5.193948213002594, 7.009890848551859, 0.24813164568415536,
            6.386858586360091, 4.338975927496364, 3.8764033804338127, 0.05913170164269621, 9.081138228865418, 0.7852867854371859,
            8.941969285817448, 5.680625151738209, 8.179137965182548, 8.179828428638071, 7.894103336576885, 7.79971268183883,
            9.178706127509368, 6.037670123704197, 7.912956574012532, 4.648659307424441, 0.5773993228675156, 7.450253028204447,
            9.534118642625213, 0.7461889030963442, 8.79717353040449, 7.765139024072302, 7.554893620914942, 6.49751467314173,
            1.6406191780526103, 9.824722374481876, 1.5739542519809402, 0.7493579424998087, 6.791492090518297, 2.259568068683473,
            7.115609604188662, 1.3356614489950402, 5.514879594708586, 4.802750241450563, 7.713743389446821, 3.1516548887038187,
            9.417409878293336, 8.62475949901677, 6.816817083468104, 9.358384873199263, 9.8954983869146, 0.2842576638737637,
            5.924149952146847, 8.946775421918659, 5.729698409313607, 5.048158077666062, 1.975024939824983, 3.079735058741253,
            4.1758041539179, 9.05521452285266, 2.685993826295583, 3.382782939863087, 4.858429856148467, 0.0805007149256931,
            3.268080222932946, 7.668117438293094, 3.790369246763067, 3.503874847315682, 1.7544140930597707, 2.4540068285954053,
            6.921118011320444, 3.493713481838104, 3.030153003730258, 7.080829295786452, 8.760513656017249, 9.38791489544405,
            3.5803781304628144, 1.2270489981648192, 1.8285377976891704, 3.7873679235522575, 9.957976579494371, 6.192862503448757,
            6.949890761208142, 1.9727850544391745, 4.0223549373369485, 0.44620824068587805, 2.7420333914250072, 1.9140089221178525,
            7.111153925379051, 1.6152792093097845, 5.0379709862392, 0.36159244574331395, 3.4809601647331023, 0.3636370720864024,
            3.440126252091671, 3.293668928290626, 0.5893456450542967, 6.398476794183242, 2.7192306506143407, 8.336823113450762,
            0.08938678495067576, 3.2395421797535997, 1.3522403520746018, 2.6878773097008124, 8.769147706796529, 6.315651897870012,
            8.135862880300483, 2.534218075317747, 3.0924367067068825, 8.457740231602354, 0.517352547113149, 9.00347956045428,
            4.483477570430564, 7.921777523907663, 5.051821965879516, 1.2691147218115073, 5.0469613806492, 6.701884266609721,
            1.22538187840041, 9.596579962395817, 5.026274234755997, 1.0834054813044591, 4.776011698824555, 9.835567023042902,
            3.9765264802131672, 2.137670559694439, 5.599131484955855, 6.146917302203406, 7.365330736353735, 1.1746067569721885,
            8.34243257532491, 0.5985558055055706, 9.113113991941288, 4.046340063398434, 0.8745944580429932, 6.672988352130133,
            8.446067386120111, 8.634360512674098, 6.955100261139812, 8.699603676053085, 6.358671212329839, 1.8547852878246063,
            4.6374367947965585, 0.7582310282425297, 2.969060772651446, 6.080708547033209, 1.205545513039551, 6.696103545357159,
            2.099321847964486, 0.007576795793629021, 8.773468410302442, 7.558579028245502, 3.5241403205993493, 5.676982414644412,
            2.070710409005639, 3.9546786810395798, 6.505050727668266, 1.6262220373192637, 1.3909179729980636, 8.661701576376263,
            0.46386348431193936, 1.9454196703065907, 9.199053094405434, 9.11888003897131, 9.947673970661583, 2.4009658574874937,
            8.472319467898354, 0.36824044998642225, 1.3991851646773668, 7.7234793361446075, 4.492672096516714, 6.471540069530115,
            8.669908876172581, 8.660449648669932, 9.513828264171822, 2.4653551585798095, 2.700129821781594, 3.4581713460038443,
            1.493264277262829, 9.512089864897233, 8.338353813875617, 6.064538108627783, 3.909773473984771, 3.06289254774454,
            7.884283137895551, 2.533709841369741, 3.579727104004834, 7.569155789446419, 6.61319290213685, 7.865628414768424,
            7.340265049690386, 1.5624887930204545, 0.27591207593261946, 7.827804402642764, 6.485221319908022, 3.768747600206491,
            8.367435529927254, 4.183752340732825, 9.919662629798196, 4.329919236576572, 9.8185280272232, 7.191903634875389,
            3.141502235037683, 5.17834846250365, 9.688505434876237, 6.240785604035219, 0.36372346321353444, 0.14100917264031065,
            6.42370479947377, 8.050697219680318, 1.358333559033087, 8.447221177444675, 9.29499095794814, 1.9990759395087465,
            2.933236645699906, 4.971413211443487, 4.738963382042333, 3.8161971854100196, 7.345799223577726, 6.763243634050479,
            8.501368281488329, 2.1498020266100717, 6.4119619666293834, 8.200583703701623, 7.717537102042829, 7.311704581822608,
            0.5873198884516417, 6.446584002905032, 3.363510835743094, 7.896377671063152, 6.8766823136074695, 4.800232204607106,
            8.87282933192784, 8.900977468456752, 8.07119352819157, 4.000529652704615, 5.137798692256847, 4.478210910203576,
            8.517180705974353, 0.5776033417656112, 5.60037609546343, 9.517910947784822, 8.388465228522799, 6.93978261625237,
            7.686637329454689, 6.880133262067475, 9.263960068983158, 0.04386931288730467, 8.450125062193225, 1.2920426936753593,
            8.7049467071756, 4.216531340452198, 2.9631999613429096, 6.67280990517355, 0.4815116787564888, 8.977958789726765,
            3.731845735997159, 0.9651955257979183, 0.11518957066299018, 4.564615644666299, 8.20820884960574, 1.4294436993622195,
            2.4849500172928307, 6.256134638065028, 9.336481508278121, 3.7945720451233256, 9.386232598115296, 3.5820938024265168,
            0.9915613708748583, 0.664085995308803, 0.13782287335055154, 2.0176287913039017, 6.156255922690343, 8.13760908135628,
            7.598929522439878, 2.8725250200793075, 0.7316196122112595, 4.736865727464437, 5.28891152275464, 1.7366508481342813,
            9.986347770750461, 3.413910320214606, 8.69467380010535, 7.065281517266712, 7.82497321538367, 5.67145115365194,
            5.305832878691952, 8.271080892517006, 1.2909446085303078, 6.639564339851463, 3.4353312356612498, 7.035199832038672,
            4.019488208012847, 1.3300543204600301, 9.463255241790453, 6.458159157630272, 2.7164896863013555, 5.229163965643203,
            6.784192407438293, 0.14188741814419759, 0.16345370283433636, 6.89129284118051, 7.443540509123717, 7.327485341702118,
            6.734932998916962, 6.260838121446902, 9.215758219231779, 0.8089930397096479, 9.9636416618406, 0.07621958697247022,
            6.707627377662577, 1.5016398248529828, 8.299800480226907, 4.899609031812223, 4.962172238046825, 7.263756956345835,
            0.885087657747381, 9.06783495250395, 7.993964005472658, 3.8547401164074833, 9.897892719862565, 7.174386474792267,
            2.704109193900266, 2.757150819502503, 5.825410288236811, 4.5693929673073255, 0.7033071248712741, 3.9992438132249317,
            8.87942506501886, 4.803913206436768, 6.7139347173682165, 2.0849220065132212, 1.6964776969627793, 9.946812710349228,
            2.4271955493017803, 9.67131028092066, 9.449425179437135, 2.266972139025216, 8.958949192647019, 6.729852983701474,
            3.546235951044625, 5.900510098847382, 9.377915589679509, 2.5850371814050446, 4.203194781314435, 8.81216931961745,
            5.172525213881114, 4.445549456415505, 5.353510038714654, 8.611959348166765, 6.977766098449948, 8.825877874630436,
            2.5886542400465773, 0.6026044085663673, 0.40095457726361694, 1.6020685679149838, 1.1285923113513652, 2.810301185484242,
            2.365438496444935, 8.353559021722493, 6.178377275071133, 0.6907412889600328, 2.033814371168927, 9.113701724540881,
            2.452429208069673, 3.704304953385723, 3.0773238966200687, 0.6848059820080921, 3.1341316800028753, 7.018791624224882,
            0.8256680223173574, 5.26922129743488, 1.0872178833503754, 4.224999674428229, 8.661559625021065, 6.3261907862621385,
            6.746736966112323, 1.139923175293428, 2.9169098916556315, 1.2849082369313092, 7.791228567769128, 6.524288737917303,
            4.482625688440331, 6.027414525516372, 5.434052836683153, 2.855606067097256, 8.361568458389367, 7.9006682852659775,
            0.9706609295459279, 9.17485694693866, 9.474308630012114, 2.4253376711538257, 4.0799084177236375, 8.464409486340895,
            4.169881677740406, 3.8395522591030007, 1.3013512355508372, 4.016683618319018, 2.0988389263649285, 1.1490683029868365,
            9.096787721189832, 2.817561031741911, 2.3734520257786764, 9.450992632378144, 0.9531382069579186, 9.095475912478875,
            1.2700200381318671, 0.9564242197339079, 2.0950825327943257, 7.876580379128962, 6.12080010681371, 1.3580674951172222,
            8.54970488972376, 6.6383324350676425, 1.4981583221755723, 4.972078089029434, 9.541723208669318, 2.9683019412747056,
            9.311125007247727, 6.429106428408069, 3.4967049566231845, 7.511951957896147, 4.014606031369771, 7.597701089840946,
            8.291005054962943, 4.432596620233432, 7.774085279486408, 5.89394855162075, 5.810987855292123, 6.108697375562789,
            8.133733837357347, 4.1644142789336875, 9.370417123853095, 4.858107406566072, 2.5034123905938, 2.925167654583569,
            0.08373695595727537, 2.1058298197940895, 9.610139663203, 2.8978645498675224, 4.904288343342875, 4.60130869970505,
            7.952170444781606, 9.504574298280492, 4.710328482285808, 8.043222526355306, 5.2489113789475965, 1.2662324045091966,
            5.558065637431538, 0.7842047743939129, 0.8974742379367662, 0.8953097222717565, 0.019727986737957393, 4.164870792162189,
            5.526752605673795, 1.2814889270855823, 8.386032770043153, 1.4659777889948034, 7.8824763985395405, 8.539842410978748,
            1.4362365496982932, 4.469506648874039, 9.481785828328835, 0.8242111924177453, 7.465284047766235, 5.183898003883909,
            1.743438727914015, 4.704181784505899, 6.306395789670515, 0.41563362202368226, 8.629844521605662, 0.5622704436233605,
            2.1987658616174666, 8.558809958489228, 0.22969901570390028, 4.68861785448392, 5.52386811064249, 1.2262920646096676,
            9.165067371541147, 8.533704282584095, 0.6701980422144238, 7.160506306615295, 2.224345239765891, 4.925303769298706,
            3.5851031365515396, 2.2388682735379306, 4.20260547756876, 6.964085941866807, 7.933687934064627, 7.328301792010519,
            9.57341043803021, 7.782991802823797, 3.2086014690444564, 5.245261677634339, 0.32125852341086003, 8.388703446017072,
            1.6208484928035338, 9.142118542281116, 5.0314009810844755, 6.399232632969137, 3.150590965293205, 7.267719803842464,
            5.163848938902826, 0.6498606133268092, 9.748235024722382, 3.2393992609266298, 9.330970117420188, 5.360356138262032,
            0.34992644854338173, 6.715653149659646, 5.3531466883729, 2.8396132275351693, 8.10790956273847, 6.673156142839769,
            2.8845497436154197, 5.549540951069854, 0.03502409934864281, 8.368747938570145, 6.481160934733163, 6.758251212977349,
            7.123951446956543, 5.506469485342306, 0.12575145182911585, 3.983287642919122, 9.92564658106881, 1.1316810634188135,
            9.429605626970076, 0.958841409239205, 0.557717814061609, 4.338751643209424, };

    //@Test
    public void test() {
        //This test fails if there are too many errors
        final Graph graph = new GraphBuilder().withPlugin(new MLXPlugin()).withScheduler(new NoopScheduler()).build();
        graph.connect(new Callback<Boolean>() {
            @Override
            public void on(Boolean result) {
                LogisticRegressionClassifierNode lrNode = (LogisticRegressionClassifierNode) graph.newTypedNode(0, 0, LogisticRegressionClassifierNode.NAME);
                lrNode.setProperty(LogisticRegressionClassifierNode.BUFFER_SIZE_KEY, Type.INT, 60);
                lrNode.setProperty(LogisticRegressionClassifierNode.LOW_ERROR_THRESH_KEY, Type.DOUBLE, 0.2);
                lrNode.setProperty(LogisticRegressionClassifierNode.HIGH_ERROR_THRESH_KEY, Type.DOUBLE, 0.3);
                lrNode.setProperty(LogisticRegressionClassifierNode.LEARNING_RATE_KEY, Type.DOUBLE, 0.001); //0.001 - looks correct
                lrNode.setProperty(LogisticRegressionClassifierNode.GD_ITERATION_THRESH_KEY, Type.INT, 1000000);
                lrNode.set(AbstractMLNode.FROM, AbstractClassifierTest.FEATURE);
                //lrNode.setL2Regularization(10);

                ClassificationJumpCallback cjc = runThroughDummyDataset(lrNode);
                lrNode.free();
                graph.disconnect(null);
                assertTrue(cjc.errors <= 1);
            }
        });
    }

    @Test
    public void testRandomGen1D() {
        //This test fails if there are too many errors
        final Graph graph = new GraphBuilder().withPlugin(new MLXPlugin()).withScheduler(new NoopScheduler()).build();
        graph.connect(new Callback<Boolean>() {
            @Override
            public void on(Boolean result) {
                LogisticRegressionClassifierNode lrNode = (LogisticRegressionClassifierNode) graph.newTypedNode(0, 0, LogisticRegressionClassifierNode.NAME);
                lrNode.setProperty(LogisticRegressionClassifierNode.BUFFER_SIZE_KEY, Type.INT, 60);
                lrNode.setProperty(LogisticRegressionClassifierNode.LOW_ERROR_THRESH_KEY, Type.DOUBLE, 0.2);
                lrNode.setProperty(LogisticRegressionClassifierNode.HIGH_ERROR_THRESH_KEY, Type.DOUBLE, 0.3);
                lrNode.setProperty(LogisticRegressionClassifierNode.LEARNING_RATE_KEY, Type.DOUBLE, 0.01);
                lrNode.setProperty(LogisticRegressionClassifierNode.GD_ITERATION_THRESH_KEY, Type.INT, 1000);
                lrNode.set(AbstractMLNode.FROM, AbstractClassifierTest.FEATURE);
                //lrNode.setL2Regularization(10);

                ClassificationJumpCallback cjc = new ClassificationJumpCallback(new String[]{AbstractClassifierTest.FEATURE});

                for (int i = 0; i < 1000; i++) {
                    double x = f[i];

                    cjc.value = new double[]{x};
                    cjc.expectedClass = (x > 5)?1:0;
                    cjc.expectedBootstrap = (i>=59)?false:true;
                    lrNode.jump(i, new Callback<Node>() {
                        @Override
                        public void on(Node result) {
                            cjc.on((AbstractClassifierSlidingWindowManagingNode) result);
                        }
                    });
                    assertTrue(cjc.errors == 0);
                }


                lrNode.free();
                graph.disconnect(null);
            }
        });
    }

}
