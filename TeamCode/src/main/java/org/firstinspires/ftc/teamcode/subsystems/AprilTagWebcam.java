package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Size;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.ArrayList;
import java.util.List;

public class AprilTagWebcam {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();
    private Telemetry telemetry;

    public AprilTagWebcam(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // e.g. Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTagProcessor.setDecimation(2);

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hwMap.get(WebcamName.class, "Vision"));
        builder.setCameraResolution(new Size(640 , 480));
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();
    }

    public void update() {
        detectedTags = aprilTagProcessor.getDetections();
    }

    public List<AprilTagDetection> getDetectedTags() {
        return detectedTags;
    }

 /*   public void displayDetetionTelemetry(AprilTagDetection detectedId) {
        if (detectedId == null) {
            telemetry.addLine("No April Tag Detected");
            return;
        }
        if (detectedId.metadata !=null) {
            telemetry.addData("Found", "ID %d (%s)", detectedId.id, detectedId.metadata.name);
            telemetry.addData("Range",  "%5.1f inches", detectedId.ftcPose.range);
            telemetry.addData("Bearing","%3.0f degrees", detectedId.ftcPose.bearing);
            telemetry.addData("Yaw","%3.0f degrees", detectedId.ftcPose.yaw);}
    }
*/
 public void telemetryAprilTag() {

     //List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
     telemetry.addData("# AprilTags Detected", detectedTags.size());

     // Step through the list of detections and display info for each one.
     for (AprilTagDetection detection : detectedTags) {
         if (detection instanceof AprilTagClusterDetection) {

             AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
             telemetry.addLine(String.format("\n==== Tag Cluster (%s)", clusterDet.metadata.name));
             telemetry.addLine(String.format("Percent tags found: %d", clusterDet.percentClusterFound));
             //telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
             //telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
             //telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
             telemetry.addData("Roll",  "%5.1f degrees", clusterDet.ftcPose.roll);
             telemetry.addData("Range",  "%5.1f inches", clusterDet.ftcPose.range);
             telemetry.addData("Bearing",  "%5.1f degrees", clusterDet.ftcPose.bearing);
             telemetry.addData("Yaw",  "%5.1f degrees", clusterDet.ftcPose.yaw);

         }
     }   // end for() loop

 }   // end method telemetryAprilTag()

    public AprilTagDetection getGoodTag(String color){
        for (AprilTagDetection detection : detectedTags) {
            if (detection instanceof AprilTagClusterDetection) {

                AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                if (clusterDet.metadata.name.contains(color)) {

                    if (clusterDet.ftcPose.roll < 90 && clusterDet.ftcPose.roll > -90) {

                        return detection;
                    }
                }
            }
        }
        return null;
    }

    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }
}