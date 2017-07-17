#
#  Be sure to run `pod spec lint wxxsdk.podspec' to ensure this is a
#  valid spec and to remove all comments including this before submitting the spec.
#
#  To learn more about Podspec attributes see http://docs.cocoapods.org/specification.html
#  To see working Podspecs in the CocoaPods repo see https://github.com/CocoaPods/Specs/
#

Pod::Spec.new do |s|
 
s.name              = "nativead"
s.version           = "1.1.2"
s.summary           = "有米原生广告开源代码"

s.homepage          = "https://github.com/youmi/nativead"
     
s.license           = { :type => "MIT", :file => "LICENSE" }

s.platform          = :ios, "7.0"
s.ios.deployment_target = "7.0"
s.author              = { "youmi" => "sdk.youmi.net" }

s.source            = { :git => "https://github.com/youmi/nativead.git", :tag => "#{s.version}" }
s.source_files      = 'YMNativeAdS-iOS/YMNativeAd/**/*.{h,m}'
#s.resource          = "videosdk/lib/UMVideo.bundle"
#s.preserve_paths    = "videosdk/lib/libUMVideoSDK.a"
#s.ios.vendored_library  = "videosdk/lib/libUMVideoSDK.a"
#s.frameworks = 'CoreImage', 'WebKit', 'Security', 'SystemConfiguration', 'UIKit','CFNetwork','MediaPlayer','StoreKit','CoreMotion','AudioToolbox','AdSupport','CoreTelephony'
#s.library = 'z','sqlite3'
  
end
 
 
