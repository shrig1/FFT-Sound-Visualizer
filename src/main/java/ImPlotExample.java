import imgui.ImVec4;
import imgui.extension.implot.ImPlot;
import imgui.flag.ImGuiCond;
import imgui.internal.ImGui;
import imgui.type.ImBoolean;

public class ImPlotExample {
    static double lb = -2*Math.PI;
    static double ub = 2*Math.PI;
    static int terms = 4096;
    static ImBoolean showDemo = new ImBoolean(true);
    static ImVec4 color = new ImVec4();

    public static Double[] xd = new Double[terms];
    public static Double[] yd = new Double[terms];

    public static Double[] xf = new Double[terms/2];
    public static Double[] yf = new Double[terms/2];

    static {
        ImPlot.createContext();
    }

    public static void show(ImBoolean showImPlotWindow) {
//        generate(terms, lb, ub);
        convertData(FFT.generate(terms, lb, ub), xd, yd);
        formatFFT(FFT.fft(FFT.generate(terms, lb, ub)), xf, yf);
//        yf = formatFFTData(FFT.fft(FFT.generate(terms, lb, ub)));
        ImGui.setNextWindowSize(900, 700, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX(), ImGui.getMainViewport().getPosY(), ImGuiCond.Once);
        if (ImGui.begin("Fourier Analysis", showImPlotWindow)) {
//            ImGui.text("This a demo for ImPlot");

            ImGui.alignTextToFramePadding();

            if (ImPlot.beginPlot("Time Domain")) {
                ImPlot.plotLine("Audio Wave", xd, yd);
//                ImPlot.plotBars("Bars", xs, ys);
                ImPlot.endPlot();
            }

            if (ImPlot.beginPlot("Frequency Domain")) {
                ImPlot.plotLine("Frequencies", xf, yf);
                ImPlot.endPlot();
            }

            if (showDemo.get()) {
                ImPlot.showDemoWindow(showDemo);
                ImGui.showDemoWindow(showDemo);
            }
            ImGui.text("Top 10 frequencies: ");
            sort();
            ImGui.text(String.format("%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n%f.5\n", xf[0], xf[1], xf[2], xf[3], xf[4], xf[5], xf[6], xf[7], xf[8], xf[9]));
        }

        ImGui.end();
    }

    public static void sort()
    {
        int n = xf.length;
        for (int i = 1; i < n; ++i) {
            Double key = yf[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && yf[j] < key) {
                xf[j + 1] = xf[j];
                j = j - 1;
            }
            xf[j + 1] = key;
        }
    }
    public static void generate(int terms, double lb, double ub) {
        double range = ub - lb;
        double i = lb;
        for(int x = 0; x < terms; i += range/terms, x++) {
            xd[x] = i;
            yd[x] = Math.sin(i);
        }
    }

    public static void convertData(Complex[] cdata, Double[] x, Double[] y) {
        int i = 0;
        double j = lb;
        for(Complex num : cdata) {
            x[i] = j;
            y[i] = num.re();
            ++i;
            j += (ub - lb) / terms;
        }
    }

    public static void formatFFT(Complex[] cdata, Double[] x, Double[] y) {
        int i = 0;
        double j = 0;
        for(Complex num : cdata) {
            if(i > terms/2 - 1) {
                break;
            }
            x[i] = j;
            y[i] = num.re();
            ++i;
            j += (ub - lb) / terms;
//            j++;
        }
    }


}